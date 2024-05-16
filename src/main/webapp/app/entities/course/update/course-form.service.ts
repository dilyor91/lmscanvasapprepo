import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICourse, NewCourse } from '../course.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICourse for edit and NewCourseFormGroupInput for create.
 */
type CourseFormGroupInput = ICourse | PartialWithRequiredKeyOf<NewCourse>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICourse | NewCourse> = Omit<T, 'courseStartDate' | 'courseEndDate'> & {
  courseStartDate?: string | null;
  courseEndDate?: string | null;
};

type CourseFormRawValue = FormValueOf<ICourse>;

type NewCourseFormRawValue = FormValueOf<NewCourse>;

type CourseFormDefaults = Pick<NewCourse, 'id' | 'courseStartDate' | 'courseEndDate' | 'published' | 'selfEnrollment' | 'status'>;

type CourseFormGroupContent = {
  id: FormControl<CourseFormRawValue['id'] | NewCourse['id']>;
  courseName: FormControl<CourseFormRawValue['courseName']>;
  courseCode: FormControl<CourseFormRawValue['courseCode']>;
  courseImagePath: FormControl<CourseFormRawValue['courseImagePath']>;
  courseStartDate: FormControl<CourseFormRawValue['courseStartDate']>;
  courseEndDate: FormControl<CourseFormRawValue['courseEndDate']>;
  courseFormat: FormControl<CourseFormRawValue['courseFormat']>;
  published: FormControl<CourseFormRawValue['published']>;
  selfEnrollment: FormControl<CourseFormRawValue['selfEnrollment']>;
  selfEnrollmentCode: FormControl<CourseFormRawValue['selfEnrollmentCode']>;
  storageQuota: FormControl<CourseFormRawValue['storageQuota']>;
  status: FormControl<CourseFormRawValue['status']>;
  account: FormControl<CourseFormRawValue['account']>;
};

export type CourseFormGroup = FormGroup<CourseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CourseFormService {
  createCourseFormGroup(course: CourseFormGroupInput = { id: null }): CourseFormGroup {
    const courseRawValue = this.convertCourseToCourseRawValue({
      ...this.getFormDefaults(),
      ...course,
    });
    return new FormGroup<CourseFormGroupContent>({
      id: new FormControl(
        { value: courseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      courseName: new FormControl(courseRawValue.courseName, {
        validators: [Validators.required],
      }),
      courseCode: new FormControl(courseRawValue.courseCode, {
        validators: [Validators.required],
      }),
      courseImagePath: new FormControl(courseRawValue.courseImagePath),
      courseStartDate: new FormControl(courseRawValue.courseStartDate, {
        validators: [Validators.required],
      }),
      courseEndDate: new FormControl(courseRawValue.courseEndDate, {
        validators: [Validators.required],
      }),
      courseFormat: new FormControl(courseRawValue.courseFormat, {
        validators: [Validators.required],
      }),
      published: new FormControl(courseRawValue.published, {
        validators: [Validators.required],
      }),
      selfEnrollment: new FormControl(courseRawValue.selfEnrollment),
      selfEnrollmentCode: new FormControl(courseRawValue.selfEnrollmentCode),
      storageQuota: new FormControl(courseRawValue.storageQuota, {
        validators: [Validators.required],
      }),
      status: new FormControl(courseRawValue.status, {
        validators: [Validators.required],
      }),
      account: new FormControl(courseRawValue.account),
    });
  }

  getCourse(form: CourseFormGroup): ICourse | NewCourse {
    return this.convertCourseRawValueToCourse(form.getRawValue() as CourseFormRawValue | NewCourseFormRawValue);
  }

  resetForm(form: CourseFormGroup, course: CourseFormGroupInput): void {
    const courseRawValue = this.convertCourseToCourseRawValue({ ...this.getFormDefaults(), ...course });
    form.reset(
      {
        ...courseRawValue,
        id: { value: courseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CourseFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      courseStartDate: currentTime,
      courseEndDate: currentTime,
      published: false,
      selfEnrollment: false,
      status: false,
    };
  }

  private convertCourseRawValueToCourse(rawCourse: CourseFormRawValue | NewCourseFormRawValue): ICourse | NewCourse {
    return {
      ...rawCourse,
      courseStartDate: dayjs(rawCourse.courseStartDate, DATE_TIME_FORMAT),
      courseEndDate: dayjs(rawCourse.courseEndDate, DATE_TIME_FORMAT),
    };
  }

  private convertCourseToCourseRawValue(
    course: ICourse | (Partial<NewCourse> & CourseFormDefaults),
  ): CourseFormRawValue | PartialWithRequiredKeyOf<NewCourseFormRawValue> {
    return {
      ...course,
      courseStartDate: course.courseStartDate ? course.courseStartDate.format(DATE_TIME_FORMAT) : undefined,
      courseEndDate: course.courseEndDate ? course.courseEndDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
