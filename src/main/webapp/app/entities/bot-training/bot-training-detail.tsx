import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bot-training.reducer';

export const BotTrainingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const botTrainingEntity = useAppSelector(state => state.botTraining.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="botTrainingDetailsHeading">Bot Training</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{botTrainingEntity.id}</dd>
          <dt>
            <span id="trainingTime">Training Time</span>
          </dt>
          <dd>
            {botTrainingEntity.trainingTime ? (
              <TextFormat value={botTrainingEntity.trainingTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="accuracy">Accuracy</span>
          </dt>
          <dd>{botTrainingEntity.accuracy}</dd>
          <dt>
            <span id="precision">Precision</span>
          </dt>
          <dd>{botTrainingEntity.precision}</dd>
          <dt>
            <span id="f1Score">F 1 Score</span>
          </dt>
          <dd>{botTrainingEntity.f1Score}</dd>
          <dt>
            <span id="active">Active</span>
          </dt>
          <dd>{botTrainingEntity.active ? 'true' : 'false'}</dd>
          <dt>Bot</dt>
          <dd>{botTrainingEntity.bot ? botTrainingEntity.bot.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/bot-training" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bot-training/${botTrainingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BotTrainingDetail;
