import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAccounts } from 'app/entities/accounts/accounts.model';
import { AccountsService } from 'app/entities/accounts/service/accounts.service';
import { CourseService } from '../service/course.service';
import { ICourse } from '../course.model';
import { CourseFormService } from './course-form.service';

import { CourseUpdateComponent } from './course-update.component';

describe('Course Management Update Component', () => {
  let comp: CourseUpdateComponent;
  let fixture: ComponentFixture<CourseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let courseFormService: CourseFormService;
  let courseService: CourseService;
  let accountsService: AccountsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CourseUpdateComponent],
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
      .overrideTemplate(CourseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CourseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    courseFormService = TestBed.inject(CourseFormService);
    courseService = TestBed.inject(CourseService);
    accountsService = TestBed.inject(AccountsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call account query and add missing value', () => {
      const course: ICourse = { id: 456 };
      const account: IAccounts = { id: 16997 };
      course.account = account;

      const accountCollection: IAccounts[] = [{ id: 3547 }];
      jest.spyOn(accountsService, 'query').mockReturnValue(of(new HttpResponse({ body: accountCollection })));
      const expectedCollection: IAccounts[] = [account, ...accountCollection];
      jest.spyOn(accountsService, 'addAccountsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(accountsService.query).toHaveBeenCalled();
      expect(accountsService.addAccountsToCollectionIfMissing).toHaveBeenCalledWith(accountCollection, account);
      expect(comp.accountsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const course: ICourse = { id: 456 };
      const account: IAccounts = { id: 19746 };
      course.account = account;

      activatedRoute.data = of({ course });
      comp.ngOnInit();

      expect(comp.accountsCollection).toContain(account);
      expect(comp.course).toEqual(course);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseFormService, 'getCourse').mockReturnValue(course);
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseFormService.getCourse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(courseService.update).toHaveBeenCalledWith(expect.objectContaining(course));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseFormService, 'getCourse').mockReturnValue({ id: null });
      jest.spyOn(courseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: course }));
      saveSubject.complete();

      // THEN
      expect(courseFormService.getCourse).toHaveBeenCalled();
      expect(courseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICourse>>();
      const course = { id: 123 };
      jest.spyOn(courseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ course });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(courseService.update).toHaveBeenCalled();
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
  });
});
