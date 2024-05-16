import { UserType } from 'app/entities/enumerations/user-type.model';
import { UserStatus } from 'app/entities/enumerations/user-status.model';

export interface IAccounts {
  id: number;
  username?: string | null;
  fullName?: string | null;
  sortableName?: string | null;
  avatarImageUrl?: string | null;
  phone?: string | null;
  locale?: string | null;
  gender?: string | null;
  userType?: keyof typeof UserType | null;
  userStatus?: keyof typeof UserStatus | null;
}

export type NewAccounts = Omit<IAccounts, 'id'> & { id: null };
