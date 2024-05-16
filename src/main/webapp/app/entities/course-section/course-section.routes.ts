import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CourseSectionComponent } from './list/course-section.component';
import { CourseSectionDetailComponent } from './detail/course-section-detail.component';
import { CourseSectionUpdateComponent } from './update/course-section-update.component';
import CourseSectionResolve from './route/course-section-routing-resolve.service';

const courseSectionRoute: Routes = [
  {
    path: '',
    component: CourseSectionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseSectionDetailComponent,
    resolve: {
      courseSection: CourseSectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseSectionUpdateComponent,
    resolve: {
      courseSection: CourseSectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseSectionUpdateComponent,
    resolve: {
      courseSection: CourseSectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default courseSectionRoute;
