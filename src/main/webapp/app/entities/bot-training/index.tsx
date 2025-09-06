import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BotTraining from './bot-training';
import BotTrainingDetail from './bot-training-detail';
import BotTrainingUpdate from './bot-training-update';
import BotTrainingDeleteDialog from './bot-training-delete-dialog';

const BotTrainingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BotTraining />} />
    <Route path="new" element={<BotTrainingUpdate />} />
    <Route path=":id">
      <Route index element={<BotTrainingDetail />} />
      <Route path="edit" element={<BotTrainingUpdate />} />
      <Route path="delete" element={<BotTrainingDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BotTrainingRoutes;
