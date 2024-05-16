import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { ICourseSection } from '../course-section.model';
import { CourseSectionService } from '../service/course-section.service';
import { CourseSectionFormService, CourseSectionFormGroup } from './course-section-form.service';

@Component({
  standalone: true,
  selector: 'jhi-course-section-update',
  templateUrl: './course-section-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CourseSectionUpdateComponent implements OnInit {
  isSaving = false;
  courseSection: ICourseSection | null = null;

  coursesSharedCollection: ICourse[] = [];

  protected courseSectionService = inject(CourseSectionService);
  protected courseSectionFormService = inject(CourseSectionFormService);
  protected courseService = inject(CourseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CourseSectionFormGroup = this.courseSectionFormService.createCourseSectionFormGroup();

  compareCourse = (o1: ICourse | null, o2: ICourse | null): boolean => this.courseService.compareCourse(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courseSection }) => {
      this.courseSection = courseSection;
      if (courseSection) {
        this.updateForm(courseSection);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courseSection = this.courseSectionFormService.getCourseSection(this.editForm);
    if (courseSection.id !== null) {
      this.subscribeToSaveResponse(this.courseSectionService.update(courseSection));
    } else {
      this.subscribeToSaveResponse(this.courseSectionService.create(courseSection));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourseSection>>): void {
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

  protected updateForm(courseSection: ICourseSection): void {
    this.courseSection = courseSection;
    this.courseSectionFormService.resetForm(this.editForm, courseSection);

    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing<ICourse>(
      this.coursesSharedCollection,
      courseSection.course,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing<ICourse>(courses, this.courseSection?.course)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }
}
