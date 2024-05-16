import { ICourseSection, NewCourseSection } from './course-section.model';

export const sampleWithRequiredData: ICourseSection = {
  id: 27631,
  sectionName: 'plus',
};

export const sampleWithPartialData: ICourseSection = {
  id: 6165,
  sectionName: 'yum',
};

export const sampleWithFullData: ICourseSection = {
  id: 9755,
  sectionName: 'ack',
};

export const sampleWithNewData: NewCourseSection = {
  sectionName: 'swing about forenenst',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
