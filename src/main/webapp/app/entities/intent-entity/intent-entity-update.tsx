import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getResponseData } from 'app/entities/response-data/response-data.reducer';
import { getEntities as getIntents } from 'app/entities/intent/intent.reducer';
import { createEntity, getEntity, reset, updateEntity } from './intent-entity.reducer';

export const IntentEntityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const responseData = useAppSelector(state => state.responseData.entities);
  const intents = useAppSelector(state => state.intent.entities);
  const intentEntityEntity = useAppSelector(state => state.intentEntity.entity);
  const loading = useAppSelector(state => state.intentEntity.loading);
  const updating = useAppSelector(state => state.intentEntity.updating);
  const updateSuccess = useAppSelector(state => state.intentEntity.updateSuccess);

  const handleClose = () => {
    navigate('/intent-entity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getResponseData({}));
    dispatch(getIntents({}));
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
      ...intentEntityEntity,
      ...values,
      missingEntityResponse: responseData.find(it => it.id.toString() === values.missingEntityResponse?.toString()),
      intent: intents.find(it => it.id.toString() === values.intent?.toString()),
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
          ...intentEntityEntity,
          missingEntityResponse: intentEntityEntity?.missingEntityResponse?.id,
          intent: intentEntityEntity?.intent?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chatbotApp.intentEntity.home.createOrEditLabel" data-cy="IntentEntityCreateUpdateHeading">
            Create or edit a Intent Entity
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="intent-entity-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Entity Name" id="intent-entity-entityName" name="entityName" data-cy="entityName" type="text" />
              <ValidatedField
                id="intent-entity-missingEntityResponse"
                name="missingEntityResponse"
                data-cy="missingEntityResponse"
                label="Missing Entity Response"
                type="select"
              >
                <option value="" key="0" />
                {responseData
                  ? responseData.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="intent-entity-intent" name="intent" data-cy="intent" label="Intent" type="select">
                <option value="" key="0" />
                {intents
                  ? intents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/intent-entity" replace color="info">
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

export default IntentEntityUpdate;
