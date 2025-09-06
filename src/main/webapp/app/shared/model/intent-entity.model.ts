import { IResponseData } from 'app/shared/model/response-data.model';
import { IIntent } from 'app/shared/model/intent.model';

export interface IIntentEntity {
  id?: number;
  entityName?: string | null;
  missingEntityResponse?: IResponseData | null;
  intent?: IIntent | null;
}

export const defaultValue: Readonly<IIntentEntity> = {};
