import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ConversationStatus } from 'app/shared/model/enumerations/conversation-status.model';
import { createEntity, getEntity, reset, updateEntity } from './conversation.reducer';

export const ConversationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const conversationEntity = useAppSelector(state => state.conversation.entity);
  const loading = useAppSelector(state => state.conversation.loading);
  const updating = useAppSelector(state => state.conversation.updating);
  const updateSuccess = useAppSelector(state => state.conversation.updateSuccess);
  const conversationStatusValues = Object.keys(ConversationStatus);

  const handleClose = () => {
    navigate('/conversation');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);

    const entity = {
      ...conversationEntity,
      ...values,
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
          startTime: displayDefaultDateTime(),
          endTime: displayDefaultDateTime(),
        }
      : {
          status: 'OPEN',
          ...conversationEntity,
          startTime: convertDateTimeFromServer(conversationEntity.startTime),
          endTime: convertDateTimeFromServer(conversationEntity.endTime),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chatbotApp.conversation.home.createOrEditLabel" data-cy="ConversationCreateUpdateHeading">
            Create or edit a Conversation
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="conversation-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Conversation Id"
                id="conversation-conversationId"
                name="conversationId"
                data-cy="conversationId"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Status" id="conversation-status" name="status" data-cy="status" type="select">
                {conversationStatusValues.map(conversationStatus => (
                  <option value={conversationStatus} key={conversationStatus}>
                    {conversationStatus}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Channel Name" id="conversation-channelName" name="channelName" data-cy="channelName" type="text" />
              <ValidatedField
                label="Start Time"
                id="conversation-startTime"
                name="startTime"
                data-cy="startTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="End Time"
                id="conversation-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/conversation" replace color="info">
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

export default ConversationUpdate;
