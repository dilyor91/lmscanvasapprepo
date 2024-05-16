import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICourseSection, NewCourseSection } from '../course-section.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICourseSection for edit and NewCourseSectionFormGroupInput for create.
 */
type CourseSectionFormGroupInput = ICourseSection | PartialWithRequiredKeyOf<NewCourseSection>;

type CourseSectionFormDefaults = Pick<NewCourseSection, 'id'>;

type CourseSectionFormGroupContent = {
  id: FormControl<ICourseSection['id'] | NewCourseSection['id']>;
  sectionName: FormControl<ICourseSection['sectionName']>;
  course: FormControl<ICourseSection['course']>;
};

export type CourseSectionFormGroup = FormGroup<CourseSectionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CourseSectionFormService {
  createCourseSectionFormGroup(courseSection: CourseSectionFormGroupInput = { id: null }): CourseSectionFormGroup {
    const courseSectionRawValue = {
      ...this.getFormDefaults(),
      ...courseSection,
    };
    return new FormGroup<CourseSectionFormGroupContent>({
      id: new FormControl(
        { value: courseSectionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sectionName: new FormControl(courseSectionRawValue.sectionName, {
        validators: [Validators.required],
      }),
      course: new FormControl(courseSectionRawValue.course),
    });
  }

  getCourseSection(form: CourseSectionFormGroup): ICourseSection | NewCourseSection {
    return form.getRawValue() as ICourseSection | NewCourseSection;
  }

  resetForm(form: CourseSectionFormGroup, courseSection: CourseSectionFormGroupInput): void {
    const courseSectionRawValue = { ...this.getFormDefaults(), ...courseSection };
    form.reset(
      {
        ...courseSectionRawValue,
        id: { value: courseSectionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CourseSectionFormDefaults {
    return {
      id: null,
    };
  }
}
