from flask import Flask, request, jsonify
import yaml
import os
import psycopg2
import spacy
from spacy.training.example import Example
import shutil
import random
from pathlib import Path

app = Flask(__name__)
env = 'dev'



@app.route('/getUtteranceDetails', methods=['POST'])
def get_utterance_details():
    data = request.get_json()
    bot_id = data.get('botId')
    sentence = data.get('sentence')
    if not bot_id or not sentence:
        return jsonify({"error": "botId and sentence are required"}), 400

    # Paths for models
    intent_model_path = get_spacy_intent_model_path(bot_id)
    ner_model_path = get_spacy_model_path(bot_id)

    # Check if models exist
    if not (os.path.isdir(intent_model_path) and os.path.isdir(ner_model_path)):
        return jsonify({"error": f"Bot {bot_id} is not trained"}), 400

    try:
        # Load models
        intent_nlp = spacy.load(intent_model_path)
        ner_nlp = spacy.load(ner_model_path)
    except Exception as e:
        return jsonify({"error": f"Model loading failed: {str(e)}"}), 500

    # Predict intent
    doc_intent = intent_nlp(sentence)
    intent_scores = {}
    for label, score in doc_intent.cats.items():
        intent_scores[label] = float(score)
    # Sort intents by confidence
    sorted_intents = sorted(intent_scores.items(), key=lambda x: x[1], reverse=True)
    top_intent, top_score = sorted_intents[0]
    top3_intents = sorted_intents[:3]

    # Predict entities
    doc_ner = ner_nlp(sentence)
    print(doc_ner)
    entities = []
    for ent in doc_ner.ents:
        entities.append({
            "text": ent.text,
            "label": ent.label_,
            "start": ent.start_char,
            "end": ent.end_char
        })

    # Prepare response
    result = {
        "intent": {
            "label": top_intent,
            "confidence": top_score,
            "top3": [
                {"label": label, "confidence": score}
                for label, score in top3_intents
            ]
        },
        "entities": entities
    }
    return jsonify(result), 200


def get_spacy_intent_model_path(bot_id):
    model_dir = os.path.join('train', str(bot_id))
    os.makedirs(model_dir, exist_ok=True)
    return os.path.join(model_dir, 'spacy_intent')

def fetch_intent_training_data(bot_id, db_details):
    # Connect to DB and fetch utterances and intent names (labels)
    conn = psycopg2.connect(
        dbname=db_details['url'].split('/')[-1],
        user=db_details['username'],
        password=db_details['password'],
        host=db_details.get('host', 'localhost'),
        port=db_details.get('port', 5432)
    )
    cur = conn.cursor()
    sql = """
        SELECT u.text, i.name
        FROM utterance u
        JOIN intent i ON u.intent_id = i.id
        WHERE i.bot_id = %s
    """
    cur.execute(sql, (bot_id,))
    results = cur.fetchall()
    cur.close()
    conn.close()
    utterances, labels = zip(*results) if results else ([], [])
    return list(utterances), list(labels)

def train_and_save_spacy_intent(bot_id, db_details):
    utterances, labels = fetch_intent_training_data(bot_id, db_details)
    if not utterances:
        print("No training data found for intent classification.")
        return

    # Get all unique intent labels
    all_labels = list(sorted(set(labels)))
    print(f"Labels: {all_labels}")

    # Prepare data for spaCy: (text, {"cats": {label: True/False ...}})
    train_data = []
    for text, label in zip(utterances, labels):
        cats = {l: (l==label) for l in all_labels}
        train_data.append((text, {"cats": cats}))

    # Create or load blank English model
    nlp = spacy.blank("en")
    if "textcat" not in nlp.pipe_names:
        textcat = nlp.add_pipe("textcat", last=True)
    else:
        textcat = nlp.get_pipe("textcat")
    for label in all_labels:
        textcat.add_label(label)

    # Convert to spaCy Examples
    examples = [Example.from_dict(nlp.make_doc(text), ann) for text, ann in train_data]
    optimizer = nlp.initialize()
    for epoch in range(10):
        random.shuffle(examples)
        losses = {}
        batches = spacy.util.minibatch(examples, size=2)
        for batch in batches:
            nlp.update(batch, losses=losses, drop=0.2)
        print(f"Epoch {epoch+1}, Losses: {losses}")

    # Save model
    model_path = get_spacy_intent_model_path(bot_id)
    if os.path.exists(model_path):
        shutil.rmtree(model_path)
    nlp.to_disk(model_path)
    print(f"spaCy intent model for bot_id={bot_id} saved to {model_path}")

def prepare_spacy_training_data(bot_id, db_details):
    """
    Returns examples = [
        ("text", {"entities":[(start, end, label)]}),
        ... ]
    """
    # Fetch annotated utterances with NER labels.
    conn = psycopg2.connect(
        dbname=db_details['url'].split('/')[-1],
        user=db_details['username'],
        password=db_details['password'],
        host=db_details.get('host', 'localhost'),
        port=db_details.get('port', 5432)
    )
    cur = conn.cursor()
    sql = """
        SELECT u.text, ie.entity_name, ui.start_index, ui.end_index
        FROM utterance_intent ui
        JOIN utterance u ON ui.utterance_id = u.id
        JOIN intent_entity ie ON ui.intent_entity_id = ie.id
        JOIN intent i ON u.intent_id = i.id
        WHERE i.bot_id = %s
        ORDER BY u.id
    """
    cur.execute(sql, (bot_id,))
    records = cur.fetchall()
    cur.close()
    conn.close()
    # Convert to spaCy format.
    examples_dict = {}
    for text, entity, start, end in records:
        if text not in examples_dict:
            examples_dict[text] = []
        examples_dict[text].append((start, end, entity))
    examples = []
    for text, entities in examples_dict.items():
        examples.append((text, {"entities": entities}))
    return examples

def get_jdbc_details():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    config_path = os.path.join(script_dir, '..', 'src', 'main', 'resources', 'config', f'application-{env}.yml')
    normalized_path = os.path.normpath(config_path)
    with open(normalized_path, 'r') as f:
        config = yaml.safe_load(f)
    return config['spring']['datasource']

def train_and_save_spacy_ner(bot_id, db_details):
    # Prepare data
    training_data = prepare_spacy_training_data(bot_id, db_details)
    if not training_data:
        print("No NER training data found, skipping spaCy model training.")
        return
        
    # Try to load an English pipeline; fallback to blank:
    try:
        nlp = spacy.load("en_core_web_sm")
        print("Loaded en_core_web_sm for base model.")
    except Exception:
        nlp = spacy.blank("en")
        print("Falling back to blank English spaCy model.")
        
    # Add NER if not present
    if 'ner' not in nlp.pipe_names:
        ner = nlp.add_pipe("ner", last=True)
    else:
        ner = nlp.get_pipe("ner")
        
    # Add entity labels
    for _, annotation in training_data:
        for start, end, label in annotation["entities"]:
            ner.add_label(label)
        
    # Convert training data to spaCy Examples
    examples = []
    for text, ann in training_data:
        print(f"Training example: {text} with annotations {ann}")
        doc = nlp.make_doc(text)
        examples.append(Example.from_dict(doc, ann))
        
    # Fine-tune
    from spacy.util import minibatch
    import random
    optimizer = nlp.resume_training()
    for itn in range(10):
        random.shuffle(examples)
        losses = {}
        batches = minibatch(examples, size=2)
        for batch in batches:
            nlp.update(batch, drop=0.5, losses=losses)
        print(f"Iteration {itn+1}, Losses: {losses}")
        
    # Save model
    model_path = get_spacy_model_path(bot_id)
    if os.path.exists(model_path):
        shutil.rmtree(model_path)
    nlp.to_disk(model_path)
    print(f"spaCy NER model for bot_id={bot_id} saved to {model_path}")

def get_spacy_model_path(bot_id):
    model_dir = os.path.join('train', str(bot_id))
    os.makedirs(model_dir, exist_ok=True)
    return os.path.join(model_dir, 'spacy')

@app.route('/train', methods=['POST'])
def train():
    data = request.get_json()
    bot_id = data.get('bot_id')
    print(f"Received bot_id: {bot_id}")
    db_details = get_jdbc_details()
    print(f"Database Details: {db_details}")
    train_and_save_spacy_intent(bot_id, db_details)
    train_and_save_spacy_ner(bot_id, db_details)
    return jsonify({"message": f"Training triggered for bot_id: {bot_id}"}), 200

# Verify if entities is correctly extracted
# import spacy
# nlp = spacy.load(r'D:\aennu_workspace\open_source_projects\chatbot\train\1001\spacy')
# nlp2 = spacy.load("en_core_web_sm")
# text = "Large veggie pizza, thin crust, olives, green peppers, low cheese, tomato sauce"
# entities = [(55, 65, 'cheese_type'), (67, 79, 'sauce_type'), (0, 5, 'pizza_size'), (6, 12, 'pizza_type'), (20, 30, 'pizza_crust'), (33, 38, 'pizza_toppings'), (40, 53, 'pizza_toppings')]
# doc = nlp.make_doc(text)
# for entity in entities:
#     print(doc.char_span(entity[0], entity[1], label=entity[2]))

if __name__ == '__main__':
    default_bot_id = "default_bot"
    # Optionally, load a spaCy model here if required for inference.
    app.run(debug=True)


# http POST http://localhost:5000/getUtteranceDetails botId='1001' sentence='I want to order a medium pizza with thin crust'  
# http POST http://localhost:5000/train bot_id='1001'