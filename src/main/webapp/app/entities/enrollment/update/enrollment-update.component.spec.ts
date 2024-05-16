import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAccounts } from 'app/entities/accounts/accounts.model';
import { AccountsService } from 'app/entities/accounts/service/accounts.service';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { CourseSectionService } from 'app/entities/course-section/service/course-section.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { IEnrollment } from '../enrollment.model';
import { EnrollmentService } from '../service/enrollment.service';
import { EnrollmentFormService } from './enrollment-form.service';

import { EnrollmentUpdateComponent } from './enrollment-update.component';

describe('Enrollment Management Update Component', () => {
  let comp: EnrollmentUpdateComponent;
  let fixture: ComponentFixture<EnrollmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enrollmentFormService: EnrollmentFormService;
  let enrollmentService: EnrollmentService;
  let accountsService: AccountsService;
  let courseSectionService: CourseSectionService;
  let courseService: CourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, EnrollmentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EnrollmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnrollmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enrollmentFormService = TestBed.inject(EnrollmentFormService);
    enrollmentService = TestBed.inject(EnrollmentService);
    accountsService = TestBed.inject(AccountsService);
    courseSectionService = TestBed.inject(CourseSectionService);
    courseService = TestBed.inject(CourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Accounts query and add missing value', () => {
      const enrollment: IEnrollment = { id: 456 };
      const account: IAccounts = { id: 29991 };
      enrollment.account = account;

      const accountsCollection: IAccounts[] = [{ id: 18459 }];
      jest.spyOn(accountsService, 'query').mockReturnValue(of(new HttpResponse({ body: accountsCollection })));
      const additionalAccounts = [account];
      const expectedCollection: IAccounts[] = [...additionalAccounts, ...accountsCollection];
      jest.spyOn(accountsService, 'addAccountsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ enrollment });
      comp.ngOnInit();

      expect(accountsService.query).toHaveBeenCalled();
      expect(accountsService.addAccountsToCollectionIfMissing).toHaveBeenCalledWith(
        accountsCollection,
        ...additionalAccounts.map(expect.objectContaining),
      );
      expect(comp.accountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CourseSection query and add missing value', () => {
      const enrollment: IEnrollment = { id: 456 };
      const courseSection: ICourseSection = { id: 15735 };
      enrollment.courseSection = courseSection;

      const courseSectionCollection: ICourseSection[] = [{ id: 29214 }];
      jest.spyOn(courseSectionService, 'query').mockReturnValue(of(new HttpResponse({ body: courseSectionCollection })));
      const additionalCourseSections = [courseSection];
      const expectedCollection: ICourseSection[] = [...additionalCourseSections, ...courseSectionCollection];
      jest.spyOn(courseSectionService, 'addCourseSectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ enrollment });
      comp.ngOnInit();

      expect(courseSectionService.query).toHaveBeenCalled();
      expect(courseSectionService.addCourseSectionToCollectionIfMissing).toHaveBeenCalledWith(
        courseSectionCollection,
        ...additionalCourseSections.map(expect.objectContaining),
      );
      expect(comp.courseSectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Course query and add missing value', () => {
      const enrollment: IEnrollment = { id: 456 };
      const course: ICourse = { id: 18589 };
      enrollment.course = course;

      const courseCollection: ICourse[] = [{ id: 18774 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [course];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ enrollment });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(
        courseCollection,
        ...additionalCourses.map(expect.objectContaining),
      );
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const enrollment: IEnrollment = { id: 456 };
      const account: IAccounts = { id: 16859 };
      enrollment.account = account;
      const courseSection: ICourseSection = { id: 23036 };
      enrollment.courseSection = courseSection;
      const course: ICourse = { id: 11850 };
      enrollment.course = course;

      activatedRoute.data = of({ enrollment });
      comp.ngOnInit();

      expect(comp.accountsSharedCollection).toContain(account);
      expect(comp.courseSectionsSharedCollection).toContain(courseSection);
      expect(comp.coursesSharedCollection).toContain(course);
      expect(comp.enrollment).toEqual(enrollment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnrollment>>();
      const enrollment = { id: 123 };
      jest.spyOn(enrollmentFormService, 'getEnrollment').mockReturnValue(enrollment);
      jest.spyOn(enrollmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enrollment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enrollment }));
      saveSubject.complete();

      // THEN
      expect(enrollmentFormService.getEnrollment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enrollmentService.update).toHaveBeenCalledWith(expect.objectContaining(enrollment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnrollment>>();
      const enrollment = { id: 123 };
      jest.spyOn(enrollmentFormService, 'getEnrollment').mockReturnValue({ id: null });
      jest.spyOn(enrollmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enrollment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enrollment }));
      saveSubject.complete();

      // THEN
      expect(enrollmentFormService.getEnrollment).toHaveBeenCalled();
      expect(enrollmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnrollment>>();
      const enrollment = { id: 123 };
      jest.spyOn(enrollmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enrollment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enrollmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAccounts', () => {
      it('Should forward to accountsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(accountsService, 'compareAccounts');
        comp.compareAccounts(entity, entity2);
        expect(accountsService.compareAccounts).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCourseSection', () => {
      it('Should forward to courseSectionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(courseSectionService, 'compareCourseSection');
        comp.compareCourseSection(entity, entity2);
        expect(courseSectionService.compareCourseSection).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCourse', () => {
      it('Should forward to courseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(courseService, 'compareCourse');
        comp.compareCourse(entity, entity2);
        expect(courseService.compareCourse).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
