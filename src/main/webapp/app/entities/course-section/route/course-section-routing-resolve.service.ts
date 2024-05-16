import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourseSection } from '../course-section.model';
import { CourseSectionService } from '../service/course-section.service';

const courseSectionResolve = (route: ActivatedRouteSnapshot): Observable<null | ICourseSection> => {
  const id = route.params['id'];
  if (id) {
    return inject(CourseSectionService)
      .find(id)
      .pipe(
        mergeMap((courseSection: HttpResponse<ICourseSection>) => {
          if (courseSection.body) {
            return of(courseSection.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default courseSectionResolve;
