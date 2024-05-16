import dayjs from 'dayjs/esm';

import { IEnrollment, NewEnrollment } from './enrollment.model';

export const sampleWithRequiredData: IEnrollment = {
  id: 18394,
  enrollmentStatus: 'ACTIVE',
};

export const sampleWithPartialData: IEnrollment = {
  id: 30145,
  enrollmentStatus: 'PENDING',
  lastActivityAt: dayjs('2024-05-16T01:54'),
  enrollmentStartAt: dayjs('2024-05-15T17:26'),
};

export const sampleWithFullData: IEnrollment = {
  id: 29188,
  enrollmentStatus: 'PENDING',
  lastActivityAt: dayjs('2024-05-15T05:12'),
  enrollmentStartAt: dayjs('2024-05-15T06:09'),
  enrollmentEndAt: dayjs('2024-05-15T13:18'),
};

export const sampleWithNewData: NewEnrollment = {
  enrollmentStatus: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
