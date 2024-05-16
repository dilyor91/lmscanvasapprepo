import dayjs from 'dayjs/esm';

import { ICourse, NewCourse } from './course.model';

export const sampleWithRequiredData: ICourse = {
  id: 14796,
  courseName: 'faint sledge paw',
  courseCode: 'excluding',
  courseStartDate: dayjs('2024-05-15T18:22'),
  courseEndDate: dayjs('2024-05-15T08:23'),
  courseFormat: 'eek',
  published: true,
  storageQuota: 25253,
  status: false,
};

export const sampleWithPartialData: ICourse = {
  id: 7032,
  courseName: 'instead woot absent',
  courseCode: 'trusty',
  courseStartDate: dayjs('2024-05-16T02:33'),
  courseEndDate: dayjs('2024-05-15T05:56'),
  courseFormat: 'cube uh-huh',
  published: false,
  selfEnrollment: false,
  storageQuota: 22007,
  status: false,
};

export const sampleWithFullData: ICourse = {
  id: 24199,
  courseName: 'wicked geez gadzooks',
  courseCode: 'cram',
  courseImagePath: 'aha enchanting boo',
  courseStartDate: dayjs('2024-05-15T18:07'),
  courseEndDate: dayjs('2024-05-15T06:47'),
  courseFormat: 'inasmuch decent vase',
  published: false,
  selfEnrollment: true,
  selfEnrollmentCode: 'worth',
  storageQuota: 20861,
  status: true,
};

export const sampleWithNewData: NewCourse = {
  courseName: 'toward',
  courseCode: 'comics now crowd',
  courseStartDate: dayjs('2024-05-16T01:43'),
  courseEndDate: dayjs('2024-05-16T00:31'),
  courseFormat: 'recall',
  published: true,
  storageQuota: 9471,
  status: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
