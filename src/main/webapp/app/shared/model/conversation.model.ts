import dayjs from 'dayjs';
import { ConversationStatus } from 'app/shared/model/enumerations/conversation-status.model';

export interface IConversation {
  id?: number;
  conversationId?: string;
  status?: keyof typeof ConversationStatus | null;
  channelName?: string | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IConversation> = {};
