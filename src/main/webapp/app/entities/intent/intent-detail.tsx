import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './intent.reducer';

export const IntentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const intentEntity = useAppSelector(state => state.intent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="intentDetailsHeading">Intent</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{intentEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{intentEntity.name}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{intentEntity.type}</dd>
          <dt>
            <span id="respType">Resp Type</span>
          </dt>
          <dd>{intentEntity.respType}</dd>
          <dt>Response Data</dt>
          <dd>{intentEntity.responseData ? intentEntity.responseData.id : ''}</dd>
          <dt>Bot</dt>
          <dd>{intentEntity.bot ? intentEntity.bot.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/intent" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/intent/${intentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default IntentDetail;
