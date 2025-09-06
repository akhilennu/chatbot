import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getConversations } from 'app/entities/conversation/conversation.reducer';
import { MessageSender } from 'app/shared/model/enumerations/message-sender.model';
import { createEntity, getEntity, reset, updateEntity } from './chat-message.reducer';

export const ChatMessageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const conversations = useAppSelector(state => state.conversation.entities);
  const chatMessageEntity = useAppSelector(state => state.chatMessage.entity);
  const loading = useAppSelector(state => state.chatMessage.loading);
  const updating = useAppSelector(state => state.chatMessage.updating);
  const updateSuccess = useAppSelector(state => state.chatMessage.updateSuccess);
  const messageSenderValues = Object.keys(MessageSender);

  const handleClose = () => {
    navigate('/chat-message');
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
    values.timestamp = convertDateTimeToServer(values.timestamp);

    const entity = {
      ...chatMessageEntity,
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
      ? {
          timestamp: displayDefaultDateTime(),
        }
      : {
          sender: 'USER',
          ...chatMessageEntity,
          timestamp: convertDateTimeFromServer(chatMessageEntity.timestamp),
          conversation: chatMessageEntity?.conversation?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="chatbotApp.chatMessage.home.createOrEditLabel" data-cy="ChatMessageCreateUpdateHeading">
            Create or edit a Chat Message
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="chat-message-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Sender" id="chat-message-sender" name="sender" data-cy="sender" type="select">
                {messageSenderValues.map(messageSender => (
                  <option value={messageSender} key={messageSender}>
                    {messageSender}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Message"
                id="chat-message-message"
                name="message"
                data-cy="message"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Timestamp"
                id="chat-message-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Channel Name" id="chat-message-channelName" name="channelName" data-cy="channelName" type="text" />
              <ValidatedField id="chat-message-conversation" name="conversation" data-cy="conversation" label="Conversation" type="select">
                <option value="" key="0" />
                {conversations
                  ? conversations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/chat-message" replace color="info">
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

export default ChatMessageUpdate;
