import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ResponseData from './response-data';
import ResponseDataDetail from './response-data-detail';
import ResponseDataUpdate from './response-data-update';
import ResponseDataDeleteDialog from './response-data-delete-dialog';

const ResponseDataRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ResponseData />} />
    <Route path="new" element={<ResponseDataUpdate />} />
    <Route path=":id">
      <Route index element={<ResponseDataDetail />} />
      <Route path="edit" element={<ResponseDataUpdate />} />
      <Route path="delete" element={<ResponseDataDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ResponseDataRoutes;
