import dayjs from 'dayjs';
import { IBot } from 'app/shared/model/bot.model';

export interface IBotTraining {
  id?: number;
  trainingTime?: dayjs.Dayjs;
  accuracy?: number | null;
  precision?: number | null;
  f1Score?: number | null;
  active?: boolean | null;
  bot?: IBot | null;
}

export const defaultValue: Readonly<IBotTraining> = {
  active: false,
};
