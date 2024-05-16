import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../course-section.test-samples';

import { CourseSectionFormService } from './course-section-form.service';

describe('CourseSection Form Service', () => {
  let service: CourseSectionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourseSectionFormService);
  });

  describe('Service methods', () => {
    describe('createCourseSectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCourseSectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sectionName: expect.any(Object),
            course: expect.any(Object),
          }),
        );
      });

      it('passing ICourseSection should create a new form with FormGroup', () => {
        const formGroup = service.createCourseSectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sectionName: expect.any(Object),
            course: expect.any(Object),
          }),
        );
      });
    });

    describe('getCourseSection', () => {
      it('should return NewCourseSection for default CourseSection initial value', () => {
        const formGroup = service.createCourseSectionFormGroup(sampleWithNewData);

        const courseSection = service.getCourseSection(formGroup) as any;

        expect(courseSection).toMatchObject(sampleWithNewData);
      });

      it('should return NewCourseSection for empty CourseSection initial value', () => {
        const formGroup = service.createCourseSectionFormGroup();

        const courseSection = service.getCourseSection(formGroup) as any;

        expect(courseSection).toMatchObject({});
      });

      it('should return ICourseSection', () => {
        const formGroup = service.createCourseSectionFormGroup(sampleWithRequiredData);

        const courseSection = service.getCourseSection(formGroup) as any;

        expect(courseSection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICourseSection should not enable id FormControl', () => {
        const formGroup = service.createCourseSectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCourseSection should disable id FormControl', () => {
        const formGroup = service.createCourseSectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
