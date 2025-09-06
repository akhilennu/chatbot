import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './intent-entity.reducer';

export const IntentEntityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const intentEntityEntity = useAppSelector(state => state.intentEntity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="intentEntityDetailsHeading">Intent Entity</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{intentEntityEntity.id}</dd>
          <dt>
            <span id="entityName">Entity Name</span>
          </dt>
          <dd>{intentEntityEntity.entityName}</dd>
          <dt>Missing Entity Response</dt>
          <dd>{intentEntityEntity.missingEntityResponse ? intentEntityEntity.missingEntityResponse.id : ''}</dd>
          <dt>Intent</dt>
          <dd>{intentEntityEntity.intent ? intentEntityEntity.intent.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/intent-entity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/intent-entity/${intentEntityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default IntentEntityDetail;
