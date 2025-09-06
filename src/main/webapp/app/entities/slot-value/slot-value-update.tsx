import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getConversations } from 'app/entities/conversation/conversation.reducer';
import { createEntity, getEntity, reset, updateEntity } from './slot-value.reducer';

export const SlotValueUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const conversations = useAppSelector(state => state.conversation.entities);
  const slotValueEntity = useAppSelector(state => state.slotValue.entity);
  const loading = useAppSelector(state => state.slotValue.loading);
  const updating = useAppSelector(state => state.slotValue.updating);
  const updateSuccess = useAppSelector(state => state.slotValue.updateSuccess);

  const handleClose = () => {
    navigate('/slot-value');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getConversations({}));
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
      ...slotValueEntity,
      ...values,
      conversation: conversations.find(it => it.id.toString() === values.conversation?.toString()),
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
          ...slotValueEntity,
          conversation: slotValueEntity?.conversation?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chatbotApp.slotValue.home.createOrEditLabel" data-cy="SlotValueCreateUpdateHeading">
            Create or edit a Slot Value
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="slot-value-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Slot Name"
                id="slot-value-slotName"
                name="slotName"
                data-cy="slotName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Slot Value"
                id="slot-value-slotValue"
                name="slotValue"
                data-cy="slotValue"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="slot-value-conversation" name="conversation" data-cy="conversation" label="Conversation" type="select">
                <option value="" key="0" />
                {conversations
                  ? conversations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/slot-value" replace color="info">
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

export default SlotValueUpdate;
