import { IResponseData } from 'app/shared/model/response-data.model';
import { IBot } from 'app/shared/model/bot.model';
import { IntentType } from 'app/shared/model/enumerations/intent-type.model';
import { IntentRespType } from 'app/shared/model/enumerations/intent-resp-type.model';

export interface IIntent {
  id?: number;
  name?: string;
  type?: keyof typeof IntentType | null;
  respType?: keyof typeof IntentRespType | null;
  responseData?: IResponseData | null;
  bot?: IBot | null;
}

export const defaultValue: Readonly<IIntent> = {};
