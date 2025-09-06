import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UtteranceIntent from './utterance-intent';
import UtteranceIntentDetail from './utterance-intent-detail';
import UtteranceIntentUpdate from './utterance-intent-update';
import UtteranceIntentDeleteDialog from './utterance-intent-delete-dialog';

const UtteranceIntentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UtteranceIntent />} />
    <Route path="new" element={<UtteranceIntentUpdate />} />
    <Route path=":id">
      <Route index element={<UtteranceIntentDetail />} />
      <Route path="edit" element={<UtteranceIntentUpdate />} />
      <Route path="delete" element={<UtteranceIntentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UtteranceIntentRoutes;
