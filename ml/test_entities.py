import psycopg2

# Fill in with your connection params
db_details = {
    'url': 'jdbc:postgresql://localhost:5432/chatbot',
    'username': 'postgres',
    'password': 'admin',
    'host': 'localhost',
    'port': 5432
}

def get_utterances_with_entities(db_details):
    # Extract the database name from the jdbc url
    dbname = db_details['url'].split('/')[-1]
    conn = psycopg2.connect(
        dbname=dbname,
        user=db_details['username'],
        password=db_details['password'],
        host=db_details.get('host', 'localhost'),
        port=db_details.get('port', 5432)
    )
    cur = conn.cursor()
    sql = """
        SELECT u.text, ui.start_index, ui.end_index, ie.entity_name, ui.id
        FROM utterance u
        JOIN utterance_intent ui ON ui.utterance_id = u.id
        JOIN intent_entity ie ON ui.intent_entity_id = ie.id
        ORDER BY u.id
    """
    cur.execute(sql)
    records = cur.fetchall()
    cur.close()
    conn.close()

    # Aggregate entities by utterance text
    from collections import defaultdict
    utterance_entities = defaultdict(list)
    for text, start, end, label, id in records:
        entity_text = text[start:end]
        # Each tuple: ((start, end, label), entity_text)
        utterance_entities[text].append( ((start, end, label), entity_text, id) )

    # Print in requested format
    for text, entities in utterance_entities.items():
        print(f'text = "{text}"')
        print(f'entities = {entities}')
        print()

get_utterances_with_entities(db_details)