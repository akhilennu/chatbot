import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './slot-value.reducer';

export const SlotValueDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const slotValueEntity = useAppSelector(state => state.slotValue.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="slotValueDetailsHeading">Slot Value</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{slotValueEntity.id}</dd>
          <dt>
            <span id="slotName">Slot Name</span>
          </dt>
          <dd>{slotValueEntity.slotName}</dd>
          <dt>
            <span id="slotValue">Slot Value</span>
          </dt>
          <dd>{slotValueEntity.slotValue}</dd>
          <dt>Conversation</dt>
          <dd>{slotValueEntity.conversation ? slotValueEntity.conversation.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/slot-value" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/slot-value/${slotValueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SlotValueDetail;
