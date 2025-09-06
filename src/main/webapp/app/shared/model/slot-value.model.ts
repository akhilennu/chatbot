import { IConversation } from 'app/shared/model/conversation.model';

export interface ISlotValue {
  id?: number;
  slotName?: string;
  slotValue?: string;
  conversation?: IConversation | null;
}

export const defaultValue: Readonly<ISlotValue> = {};
