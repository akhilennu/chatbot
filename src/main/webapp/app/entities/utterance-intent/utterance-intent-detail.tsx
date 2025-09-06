import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './utterance-intent.reducer';

export const UtteranceIntentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const utteranceIntentEntity = useAppSelector(state => state.utteranceIntent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="utteranceIntentDetailsHeading">Utterance Intent</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{utteranceIntentEntity.id}</dd>
          <dt>
            <span id="startIndex">Start Index</span>
          </dt>
          <dd>{utteranceIntentEntity.startIndex}</dd>
          <dt>
            <span id="endIndex">End Index</span>
          </dt>
          <dd>{utteranceIntentEntity.endIndex}</dd>
          <dt>Utterance</dt>
          <dd>{utteranceIntentEntity.utterance ? utteranceIntentEntity.utterance.id : ''}</dd>
          <dt>Intent Entity</dt>
          <dd>{utteranceIntentEntity.intentEntity ? utteranceIntentEntity.intentEntity.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/utterance-intent" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/utterance-intent/${utteranceIntentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UtteranceIntentDetail;
