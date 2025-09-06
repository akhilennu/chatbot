import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SlotValue from './slot-value';
import SlotValueDetail from './slot-value-detail';
import SlotValueUpdate from './slot-value-update';
import SlotValueDeleteDialog from './slot-value-delete-dialog';

const SlotValueRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SlotValue />} />
    <Route path="new" element={<SlotValueUpdate />} />
    <Route path=":id">
      <Route index element={<SlotValueDetail />} />
      <Route path="edit" element={<SlotValueUpdate />} />
      <Route path="delete" element={<SlotValueDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SlotValueRoutes;
