import dayjs from 'dayjs';
import { IConversation } from 'app/shared/model/conversation.model';
import { MessageSender } from 'app/shared/model/enumerations/message-sender.model';

export interface IChatMessage {
  id?: number;
  sender?: keyof typeof MessageSender;
  message?: string;
  timestamp?: dayjs.Dayjs;
  channelName?: string | null;
  conversation?: IConversation | null;
}

export const defaultValue: Readonly<IChatMessage> = {};
