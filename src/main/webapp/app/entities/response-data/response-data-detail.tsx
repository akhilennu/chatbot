import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './response-data.reducer';

export const ResponseDataDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const responseDataEntity = useAppSelector(state => state.responseData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="responseDataDetailsHeading">Response Data</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{responseDataEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{responseDataEntity.type}</dd>
          <dt>
            <span id="content">Content</span>
          </dt>
          <dd>{responseDataEntity.content}</dd>
          <dt>
            <span id="channelName">Channel Name</span>
          </dt>
          <dd>{responseDataEntity.channelName}</dd>
        </dl>
        <Button tag={Link} to="/response-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/response-data/${responseDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ResponseDataDetail;
