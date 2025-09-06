import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './conversation.reducer';

export const ConversationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const conversationEntity = useAppSelector(state => state.conversation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="conversationDetailsHeading">Conversation</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{conversationEntity.id}</dd>
          <dt>
            <span id="conversationId">Conversation Id</span>
          </dt>
          <dd>{conversationEntity.conversationId}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{conversationEntity.status}</dd>
          <dt>
            <span id="channelName">Channel Name</span>
          </dt>
          <dd>{conversationEntity.channelName}</dd>
          <dt>
            <span id="startTime">Start Time</span>
          </dt>
          <dd>
            {conversationEntity.startTime ? <TextFormat value={conversationEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endTime">End Time</span>
          </dt>
          <dd>
            {conversationEntity.endTime ? <TextFormat value={conversationEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/conversation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/conversation/${conversationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ConversationDetail;
