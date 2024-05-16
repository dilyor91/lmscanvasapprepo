import dayjs from 'dayjs/esm';
import { IAccounts } from 'app/entities/accounts/accounts.model';

export interface ICourse {
  id: number;
  courseName?: string | null;
  courseCode?: string | null;
  courseImagePath?: string | null;
  courseStartDate?: dayjs.Dayjs | null;
  courseEndDate?: dayjs.Dayjs | null;
  courseFormat?: string | null;
  published?: boolean | null;
  selfEnrollment?: boolean | null;
  selfEnrollmentCode?: string | null;
  storageQuota?: number | null;
  status?: boolean | null;
  account?: Pick<IAccounts, 'id'> | null;
}

export type NewCourse = Omit<ICourse, 'id'> & { id: null };
