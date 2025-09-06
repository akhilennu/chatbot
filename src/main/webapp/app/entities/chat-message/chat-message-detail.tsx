import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './chat-message.reducer';

export const ChatMessageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const chatMessageEntity = useAppSelector(state => state.chatMessage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chatMessageDetailsHeading">Chat Message</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{chatMessageEntity.id}</dd>
          <dt>
            <span id="sender">Sender</span>
          </dt>
          <dd>{chatMessageEntity.sender}</dd>
          <dt>
            <span id="message">Message</span>
          </dt>
          <dd>{chatMessageEntity.message}</dd>
          <dt>
            <span id="timestamp">Timestamp</span>
          </dt>
          <dd>
            {chatMessageEntity.timestamp ? <TextFormat value={chatMessageEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="channelName">Channel Name</span>
          </dt>
          <dd>{chatMessageEntity.channelName}</dd>
          <dt>Conversation</dt>
          <dd>{chatMessageEntity.conversation ? chatMessageEntity.conversation.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/chat-message" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chat-message/${chatMessageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChatMessageDetail;
