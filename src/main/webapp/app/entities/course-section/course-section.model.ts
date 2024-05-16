import { ICourse } from 'app/entities/course/course.model';

export interface ICourseSection {
  id: number;
  sectionName?: string | null;
  course?: Pick<ICourse, 'id'> | null;
}

export type NewCourseSection = Omit<ICourseSection, 'id'> & { id: null };
