import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUtterances } from 'app/entities/utterance/utterance.reducer';
import { getEntities as getIntentEntities } from 'app/entities/intent-entity/intent-entity.reducer';
import { createEntity, getEntity, reset, updateEntity } from './utterance-intent.reducer';

export const UtteranceIntentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utterances = useAppSelector(state => state.utterance.entities);
  const intentEntities = useAppSelector(state => state.intentEntity.entities);
  const utteranceIntentEntity = useAppSelector(state => state.utteranceIntent.entity);
  const loading = useAppSelector(state => state.utteranceIntent.loading);
  const updating = useAppSelector(state => state.utteranceIntent.updating);
  const updateSuccess = useAppSelector(state => state.utteranceIntent.updateSuccess);

  const handleClose = () => {
    navigate('/utterance-intent');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUtterances({}));
    dispatch(getIntentEntities({}));
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
    if (values.startIndex !== undefined && typeof values.startIndex !== 'number') {
      values.startIndex = Number(values.startIndex);
    }
    if (values.endIndex !== undefined && typeof values.endIndex !== 'number') {
      values.endIndex = Number(values.endIndex);
    }

    const entity = {
      ...utteranceIntentEntity,
      ...values,
      utterance: utterances.find(it => it.id.toString() === values.utterance?.toString()),
      intentEntity: intentEntities.find(it => it.id.toString() === values.intentEntity?.toString()),
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
          ...utteranceIntentEntity,
          utterance: utteranceIntentEntity?.utterance?.id,
          intentEntity: utteranceIntentEntity?.intentEntity?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chatbotApp.utteranceIntent.home.createOrEditLabel" data-cy="UtteranceIntentCreateUpdateHeading">
            Create or edit a Utterance Intent
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
                <ValidatedField name="id" required readOnly id="utterance-intent-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Start Index"
                id="utterance-intent-startIndex"
                name="startIndex"
                data-cy="startIndex"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="End Index"
                id="utterance-intent-endIndex"
                name="endIndex"
                data-cy="endIndex"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField id="utterance-intent-utterance" name="utterance" data-cy="utterance" label="Utterance" type="select">
                <option value="" key="0" />
                {utterances
                  ? utterances.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="utterance-intent-intentEntity"
                name="intentEntity"
                data-cy="intentEntity"
                label="Intent Entity"
                type="select"
              >
                <option value="" key="0" />
                {intentEntities
                  ? intentEntities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/utterance-intent" replace color="info">
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

export default UtteranceIntentUpdate;
