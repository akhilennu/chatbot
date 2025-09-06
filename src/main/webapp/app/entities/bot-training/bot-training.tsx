import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './bot-training.reducer';

export const BotTraining = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const botTrainingList = useAppSelector(state => state.botTraining.entities);
  const loading = useAppSelector(state => state.botTraining.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="bot-training-heading" data-cy="BotTrainingHeading">
        Bot Trainings
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/bot-training/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Bot Training
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {botTrainingList && botTrainingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('trainingTime')}>
                  Training Time <FontAwesomeIcon icon={getSortIconByFieldName('trainingTime')} />
                </th>
                <th className="hand" onClick={sort('accuracy')}>
                  Accuracy <FontAwesomeIcon icon={getSortIconByFieldName('accuracy')} />
                </th>
                <th className="hand" onClick={sort('precision')}>
                  Precision <FontAwesomeIcon icon={getSortIconByFieldName('precision')} />
                </th>
                <th className="hand" onClick={sort('f1Score')}>
                  F 1 Score <FontAwesomeIcon icon={getSortIconByFieldName('f1Score')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  Active <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th>
                  Bot <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {botTrainingList.map((botTraining, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/bot-training/${botTraining.id}`} color="link" size="sm">
                      {botTraining.id}
                    </Button>
                  </td>
                  <td>
                    {botTraining.trainingTime ? <TextFormat type="date" value={botTraining.trainingTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{botTraining.accuracy}</td>
                  <td>{botTraining.precision}</td>
                  <td>{botTraining.f1Score}</td>
                  <td>{botTraining.active ? 'true' : 'false'}</td>
                  <td>{botTraining.bot ? <Link to={`/bot/${botTraining.bot.id}`}>{botTraining.bot.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/bot-training/${botTraining.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/bot-training/${botTraining.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/bot-training/${botTraining.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Bot Trainings found</div>
        )}
      </div>
    </div>
  );
};

export default BotTraining;
