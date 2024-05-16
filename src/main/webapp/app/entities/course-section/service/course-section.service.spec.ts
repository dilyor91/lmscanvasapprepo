import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICourseSection } from '../course-section.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../course-section.test-samples';

import { CourseSectionService } from './course-section.service';

const requireRestSample: ICourseSection = {
  ...sampleWithRequiredData,
};

describe('CourseSection Service', () => {
  let service: CourseSectionService;
  let httpMock: HttpTestingController;
  let expectedResult: ICourseSection | ICourseSection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CourseSectionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CourseSection', () => {
      const courseSection = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(courseSection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CourseSection', () => {
      const courseSection = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(courseSection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CourseSection', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CourseSection', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CourseSection', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCourseSectionToCollectionIfMissing', () => {
      it('should add a CourseSection to an empty array', () => {
        const courseSection: ICourseSection = sampleWithRequiredData;
        expectedResult = service.addCourseSectionToCollectionIfMissing([], courseSection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(courseSection);
      });

      it('should not add a CourseSection to an array that contains it', () => {
        const courseSection: ICourseSection = sampleWithRequiredData;
        const courseSectionCollection: ICourseSection[] = [
          {
            ...courseSection,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCourseSectionToCollectionIfMissing(courseSectionCollection, courseSection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CourseSection to an array that doesn't contain it", () => {
        const courseSection: ICourseSection = sampleWithRequiredData;
        const courseSectionCollection: ICourseSection[] = [sampleWithPartialData];
        expectedResult = service.addCourseSectionToCollectionIfMissing(courseSectionCollection, courseSection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(courseSection);
      });

      it('should add only unique CourseSection to an array', () => {
        const courseSectionArray: ICourseSection[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const courseSectionCollection: ICourseSection[] = [sampleWithRequiredData];
        expectedResult = service.addCourseSectionToCollectionIfMissing(courseSectionCollection, ...courseSectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const courseSection: ICourseSection = sampleWithRequiredData;
        const courseSection2: ICourseSection = sampleWithPartialData;
        expectedResult = service.addCourseSectionToCollectionIfMissing([], courseSection, courseSection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(courseSection);
        expect(expectedResult).toContain(courseSection2);
      });

      it('should accept null and undefined values', () => {
        const courseSection: ICourseSection = sampleWithRequiredData;
        expectedResult = service.addCourseSectionToCollectionIfMissing([], null, courseSection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(courseSection);
      });

      it('should return initial array if no CourseSection is added', () => {
        const courseSectionCollection: ICourseSection[] = [sampleWithRequiredData];
        expectedResult = service.addCourseSectionToCollectionIfMissing(courseSectionCollection, undefined, null);
        expect(expectedResult).toEqual(courseSectionCollection);
      });
    });

    describe('compareCourseSection', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCourseSection(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCourseSection(entity1, entity2);
        const compareResult2 = service.compareCourseSection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCourseSection(entity1, entity2);
        const compareResult2 = service.compareCourseSection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCourseSection(entity1, entity2);
        const compareResult2 = service.compareCourseSection(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
