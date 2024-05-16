import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'lmscanvasappApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'course',
    data: { pageTitle: 'lmscanvasappApp.course.home.title' },
    loadChildren: () => import('./course/course.routes'),
  },
  {
    path: 'course-section',
    data: { pageTitle: 'lmscanvasappApp.courseSection.home.title' },
    loadChildren: () => import('./course-section/course-section.routes'),
  },
  {
    path: 'accounts',
    data: { pageTitle: 'lmscanvasappApp.accounts.home.title' },
    loadChildren: () => import('./accounts/accounts.routes'),
  },
  {
    path: 'enrollment',
    data: { pageTitle: 'lmscanvasappApp.enrollment.home.title' },
    loadChildren: () => import('./enrollment/enrollment.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
