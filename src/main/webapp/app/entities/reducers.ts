import bot from 'app/entities/bot/bot.reducer';
import intent from 'app/entities/intent/intent.reducer';
import responseData from 'app/entities/response-data/response-data.reducer';
import intentEntity from 'app/entities/intent-entity/intent-entity.reducer';
import utterance from 'app/entities/utterance/utterance.reducer';
import conversation from 'app/entities/conversation/conversation.reducer';
import chatMessage from 'app/entities/chat-message/chat-message.reducer';
import slotValue from 'app/entities/slot-value/slot-value.reducer';
import botTraining from 'app/entities/bot-training/bot-training.reducer';
import utteranceIntent from 'app/entities/utterance-intent/utterance-intent.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  bot,
  intent,
  responseData,
  intentEntity,
  utterance,
  conversation,
  chatMessage,
  slotValue,
  botTraining,
  utteranceIntent,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
