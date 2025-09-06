export interface IResponseData {
  id?: number;
  type?: string | null;
  content?: string | null;
  channelName?: string | null;
}

export const defaultValue: Readonly<IResponseData> = {};
