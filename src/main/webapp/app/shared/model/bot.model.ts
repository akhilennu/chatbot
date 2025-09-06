export interface IBot {
  id?: number;
  name?: string;
  description?: string | null;
}

export const defaultValue: Readonly<IBot> = {};
