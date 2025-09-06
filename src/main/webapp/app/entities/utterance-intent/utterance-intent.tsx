import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './utterance-intent.reducer';

export const UtteranceIntent = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const utteranceIntentList = useAppSelector(state => state.utteranceIntent.entities);
  const loading = useAppSelector(state => state.utteranceIntent.loading);

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
      <h2 id="utterance-intent-heading" data-cy="UtteranceIntentHeading">
        Utterance Intents
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/utterance-intent/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Utterance Intent
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {utteranceIntentList && utteranceIntentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startIndex')}>
                  Start Index <FontAwesomeIcon icon={getSortIconByFieldName('startIndex')} />
                </th>
                <th className="hand" onClick={sort('endIndex')}>
                  End Index <FontAwesomeIcon icon={getSortIconByFieldName('endIndex')} />
                </th>
                <th>
                  Utterance <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Intent Entity <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {utteranceIntentList.map((utteranceIntent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/utterance-intent/${utteranceIntent.id}`} color="link" size="sm">
                      {utteranceIntent.id}
                    </Button>
                  </td>
                  <td>{utteranceIntent.startIndex}</td>
                  <td>{utteranceIntent.endIndex}</td>
                  <td>
                    {utteranceIntent.utterance ? (
                      <Link to={`/utterance/${utteranceIntent.utterance.id}`}>{utteranceIntent.utterance.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {utteranceIntent.intentEntity ? (
                      <Link to={`/intent-entity/${utteranceIntent.intentEntity.id}`}>{utteranceIntent.intentEntity.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/utterance-intent/${utteranceIntent.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/utterance-intent/${utteranceIntent.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/utterance-intent/${utteranceIntent.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Utterance Intents found</div>
        )}
      </div>
    </div>
  );
};

export default UtteranceIntent;
