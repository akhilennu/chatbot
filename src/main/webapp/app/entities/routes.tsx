import React from 'react';
import { Route } from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Bot from './bot';
import Intent from './intent';
import ResponseData from './response-data';
import IntentEntity from './intent-entity';
import Utterance from './utterance';
import Conversation from './conversation';
import ChatMessage from './chat-message';
import SlotValue from './slot-value';
import BotTraining from './bot-training';
import UtteranceIntent from './utterance-intent';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="bot/*" element={<Bot />} />
        <Route path="intent/*" element={<Intent />} />
        <Route path="response-data/*" element={<ResponseData />} />
        <Route path="intent-entity/*" element={<IntentEntity />} />
        <Route path="utterance/*" element={<Utterance />} />
        <Route path="conversation/*" element={<Conversation />} />
        <Route path="chat-message/*" element={<ChatMessage />} />
        <Route path="slot-value/*" element={<SlotValue />} />
        <Route path="bot-training/*" element={<BotTraining />} />
        <Route path="utterance-intent/*" element={<UtteranceIntent />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
