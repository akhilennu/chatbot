import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBots } from 'app/entities/bot/bot.reducer';
import { createEntity, getEntity, reset, updateEntity } from './bot-training.reducer';

export const BotTrainingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bots = useAppSelector(state => state.bot.entities);
  const botTrainingEntity = useAppSelector(state => state.botTraining.entity);
  const loading = useAppSelector(state => state.botTraining.loading);
  const updating = useAppSelector(state => state.botTraining.updating);
  const updateSuccess = useAppSelector(state => state.botTraining.updateSuccess);

  const handleClose = () => {
    navigate('/bot-training');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBots({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.trainingTime = convertDateTimeToServer(values.trainingTime);
    if (values.accuracy !== undefined && typeof values.accuracy !== 'number') {
      values.accuracy = Number(values.accuracy);
    }
    if (values.precision !== undefined && typeof values.precision !== 'number') {
      values.precision = Number(values.precision);
    }
    if (values.f1Score !== undefined && typeof values.f1Score !== 'number') {
      values.f1Score = Number(values.f1Score);
    }

    const entity = {
      ...botTrainingEntity,
      ...values,
      bot: bots.find(it => it.id.toString() === values.bot?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          trainingTime: displayDefaultDateTime(),
        }
      : {
          ...botTrainingEntity,
          trainingTime: convertDateTimeFromServer(botTrainingEntity.trainingTime),
          bot: botTrainingEntity?.bot?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chatbotApp.botTraining.home.createOrEditLabel" data-cy="BotTrainingCreateUpdateHeading">
            Create or edit a Bot Training
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="bot-training-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Training Time"
                id="bot-training-trainingTime"
                name="trainingTime"
                data-cy="trainingTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Accuracy" id="bot-training-accuracy" name="accuracy" data-cy="accuracy" type="text" />
              <ValidatedField label="Precision" id="bot-training-precision" name="precision" data-cy="precision" type="text" />
              <ValidatedField label="F 1 Score" id="bot-training-f1Score" name="f1Score" data-cy="f1Score" type="text" />
              <ValidatedField label="Active" id="bot-training-active" name="active" data-cy="active" check type="checkbox" />
              <ValidatedField id="bot-training-bot" name="bot" data-cy="bot" label="Bot" type="select">
                <option value="" key="0" />
                {bots
                  ? bots.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bot-training" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BotTrainingUpdate;
