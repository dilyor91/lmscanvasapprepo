import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAccounts } from 'app/entities/accounts/accounts.model';
import { AccountsService } from 'app/entities/accounts/service/accounts.service';
import { ICourseSection } from 'app/entities/course-section/course-section.model';
import { CourseSectionService } from 'app/entities/course-section/service/course-section.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { EnrollmentStatusEnum } from 'app/entities/enumerations/enrollment-status-enum.model';
import { EnrollmentService } from '../service/enrollment.service';
import { IEnrollment } from '../enrollment.model';
import { EnrollmentFormService, EnrollmentFormGroup } from './enrollment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-enrollment-update',
  templateUrl: './enrollment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnrollmentUpdateComponent implements OnInit {
  isSaving = false;
  enrollment: IEnrollment | null = null;
  enrollmentStatusEnumValues = Object.keys(EnrollmentStatusEnum);

  accountsSharedCollection: IAccounts[] = [];
  courseSectionsSharedCollection: ICourseSection[] = [];
  coursesSharedCollection: ICourse[] = [];

  protected enrollmentService = inject(EnrollmentService);
  protected enrollmentFormService = inject(EnrollmentFormService);
  protected accountsService = inject(AccountsService);
  protected courseSectionService = inject(CourseSectionService);
  protected courseService = inject(CourseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EnrollmentFormGroup = this.enrollmentFormService.createEnrollmentFormGroup();

  compareAccounts = (o1: IAccounts | null, o2: IAccounts | null): boolean => this.accountsService.compareAccounts(o1, o2);

  compareCourseSection = (o1: ICourseSection | null, o2: ICourseSection | null): boolean =>
    this.courseSectionService.compareCourseSection(o1, o2);

  compareCourse = (o1: ICourse | null, o2: ICourse | null): boolean => this.courseService.compareCourse(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enrollment }) => {
      this.enrollment = enrollment;
      if (enrollment) {
        this.updateForm(enrollment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enrollment = this.enrollmentFormService.getEnrollment(this.editForm);
    if (enrollment.id !== null) {
      this.subscribeToSaveResponse(this.enrollmentService.update(enrollment));
    } else {
      this.subscribeToSaveResponse(this.enrollmentService.create(enrollment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnrollment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(enrollment: IEnrollment): void {
    this.enrollment = enrollment;
    this.enrollmentFormService.resetForm(this.editForm, enrollment);

    this.accountsSharedCollection = this.accountsService.addAccountsToCollectionIfMissing<IAccounts>(
      this.accountsSharedCollection,
      enrollment.account,
    );
    this.courseSectionsSharedCollection = this.courseSectionService.addCourseSectionToCollectionIfMissing<ICourseSection>(
      this.courseSectionsSharedCollection,
      enrollment.courseSection,
    );
    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing<ICourse>(
      this.coursesSharedCollection,
      enrollment.course,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.accountsService
      .query()
      .pipe(map((res: HttpResponse<IAccounts[]>) => res.body ?? []))
      .pipe(
        map((accounts: IAccounts[]) =>
          this.accountsService.addAccountsToCollectionIfMissing<IAccounts>(accounts, this.enrollment?.account),
        ),
      )
      .subscribe((accounts: IAccounts[]) => (this.accountsSharedCollection = accounts));

    this.courseSectionService
      .query()
      .pipe(map((res: HttpResponse<ICourseSection[]>) => res.body ?? []))
      .pipe(
        map((courseSections: ICourseSection[]) =>
          this.courseSectionService.addCourseSectionToCollectionIfMissing<ICourseSection>(courseSections, this.enrollment?.courseSection),
        ),
      )
      .subscribe((courseSections: ICourseSection[]) => (this.courseSectionsSharedCollection = courseSections));

    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing<ICourse>(courses, this.enrollment?.course)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }
}
