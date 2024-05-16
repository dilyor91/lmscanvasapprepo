import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAccounts } from 'app/entities/accounts/accounts.model';
import { AccountsService } from 'app/entities/accounts/service/accounts.service';
import { ICourse } from '../course.model';
import { CourseService } from '../service/course.service';
import { CourseFormService, CourseFormGroup } from './course-form.service';

@Component({
  standalone: true,
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;
  course: ICourse | null = null;

  accountsCollection: IAccounts[] = [];

  protected courseService = inject(CourseService);
  protected courseFormService = inject(CourseFormService);
  protected accountsService = inject(AccountsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CourseFormGroup = this.courseFormService.createCourseFormGroup();

  compareAccounts = (o1: IAccounts | null, o2: IAccounts | null): boolean => this.accountsService.compareAccounts(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      this.course = course;
      if (course) {
        this.updateForm(course);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.courseFormService.getCourse(this.editForm);
    if (course.id !== null) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
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

  protected updateForm(course: ICourse): void {
    this.course = course;
    this.courseFormService.resetForm(this.editForm, course);

    this.accountsCollection = this.accountsService.addAccountsToCollectionIfMissing<IAccounts>(this.accountsCollection, course.account);
  }

  protected loadRelationshipsOptions(): void {
    this.accountsService
      .query({ filter: 'course-is-null' })
      .pipe(map((res: HttpResponse<IAccounts[]>) => res.body ?? []))
      .pipe(
        map((accounts: IAccounts[]) => this.accountsService.addAccountsToCollectionIfMissing<IAccounts>(accounts, this.course?.account)),
      )
      .subscribe((accounts: IAccounts[]) => (this.accountsCollection = accounts));
  }
}
