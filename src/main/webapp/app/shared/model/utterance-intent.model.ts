import { IUtterance } from 'app/shared/model/utterance.model';
import { IIntentEntity } from 'app/shared/model/intent-entity.model';

export interface IUtteranceIntent {
  id?: number;
  startIndex?: number;
  endIndex?: number;
  utterance?: IUtterance | null;
  intentEntity?: IIntentEntity | null;
}

export const defaultValue: Readonly<IUtteranceIntent> = {};
