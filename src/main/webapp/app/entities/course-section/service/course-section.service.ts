import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourseSection, NewCourseSection } from '../course-section.model';

export type PartialUpdateCourseSection = Partial<ICourseSection> & Pick<ICourseSection, 'id'>;

export type EntityResponseType = HttpResponse<ICourseSection>;
export type EntityArrayResponseType = HttpResponse<ICourseSection[]>;

@Injectable({ providedIn: 'root' })
export class CourseSectionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/course-sections');

  create(courseSection: NewCourseSection): Observable<EntityResponseType> {
    return this.http.post<ICourseSection>(this.resourceUrl, courseSection, { observe: 'response' });
  }

  update(courseSection: ICourseSection): Observable<EntityResponseType> {
    return this.http.put<ICourseSection>(`${this.resourceUrl}/${this.getCourseSectionIdentifier(courseSection)}`, courseSection, {
      observe: 'response',
    });
  }

  partialUpdate(courseSection: PartialUpdateCourseSection): Observable<EntityResponseType> {
    return this.http.patch<ICourseSection>(`${this.resourceUrl}/${this.getCourseSectionIdentifier(courseSection)}`, courseSection, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICourseSection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourseSection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCourseSectionIdentifier(courseSection: Pick<ICourseSection, 'id'>): number {
    return courseSection.id;
  }

  compareCourseSection(o1: Pick<ICourseSection, 'id'> | null, o2: Pick<ICourseSection, 'id'> | null): boolean {
    return o1 && o2 ? this.getCourseSectionIdentifier(o1) === this.getCourseSectionIdentifier(o2) : o1 === o2;
  }

  addCourseSectionToCollectionIfMissing<Type extends Pick<ICourseSection, 'id'>>(
    courseSectionCollection: Type[],
    ...courseSectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const courseSections: Type[] = courseSectionsToCheck.filter(isPresent);
    if (courseSections.length > 0) {
      const courseSectionCollectionIdentifiers = courseSectionCollection.map(courseSectionItem =>
        this.getCourseSectionIdentifier(courseSectionItem),
      );
      const courseSectionsToAdd = courseSections.filter(courseSectionItem => {
        const courseSectionIdentifier = this.getCourseSectionIdentifier(courseSectionItem);
        if (courseSectionCollectionIdentifiers.includes(courseSectionIdentifier)) {
          return false;
        }
        courseSectionCollectionIdentifiers.push(courseSectionIdentifier);
        return true;
      });
      return [...courseSectionsToAdd, ...courseSectionCollection];
    }
    return courseSectionCollection;
  }
}
