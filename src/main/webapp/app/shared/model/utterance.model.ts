import { IIntent } from 'app/shared/model/intent.model';

export interface IUtterance {
  id?: number;
  text?: string;
  intent?: IIntent | null;
}

export const defaultValue: Readonly<IUtterance> = {};
