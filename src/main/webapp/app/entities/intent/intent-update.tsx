import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getResponseData } from 'app/entities/response-data/response-data.reducer';
import { getEntities as getBots } from 'app/entities/bot/bot.reducer';
import { IntentType } from 'app/shared/model/enumerations/intent-type.model';
import { IntentRespType } from 'app/shared/model/enumerations/intent-resp-type.model';
import { createEntity, getEntity, reset, updateEntity } from './intent.reducer';

export const IntentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const responseData = useAppSelector(state => state.responseData.entities);
  const bots = useAppSelector(state => state.bot.entities);
  const intentEntity = useAppSelector(state => state.intent.entity);
  const loading = useAppSelector(state => state.intent.loading);
  const updating = useAppSelector(state => state.intent.updating);
  const updateSuccess = useAppSelector(state => state.intent.updateSuccess);
  const intentTypeValues = Object.keys(IntentType);
  const intentRespTypeValues = Object.keys(IntentRespType);

  const handleClose = () => {
    navigate(`/intent${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getResponseData({}));
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

    const entity = {
      ...intentEntity,
      ...values,
      responseData: responseData.find(it => it.id.toString() === values.responseData?.toString()),
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
      ? {}
      : {
          type: 'DEFAULT',
          respType: 'MISSING_ENTITY_FLOW',
          ...intentEntity,
          responseData: intentEntity?.responseData?.id,
          bot: intentEntity?.bot?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chatbotApp.intent.home.createOrEditLabel" data-cy="IntentCreateUpdateHeading">
            Create or edit a Intent
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="intent-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="intent-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Type" id="intent-type" name="type" data-cy="type" type="select">
                {intentTypeValues.map(intentType => (
                  <option value={intentType} key={intentType}>
                    {intentType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Resp Type" id="intent-respType" name="respType" data-cy="respType" type="select">
                {intentRespTypeValues.map(intentRespType => (
                  <option value={intentRespType} key={intentRespType}>
                    {intentRespType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="intent-responseData" name="responseData" data-cy="responseData" label="Response Data" type="select">
                <option value="" key="0" />
                {responseData
                  ? responseData.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="intent-bot" name="bot" data-cy="bot" label="Bot" type="select">
                <option value="" key="0" />
                {bots
                  ? bots.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/intent" replace color="info">
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

export default IntentUpdate;
