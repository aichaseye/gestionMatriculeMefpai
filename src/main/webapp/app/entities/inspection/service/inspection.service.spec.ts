import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeInspection } from 'app/entities/enumerations/type-inspection.model';
import { IInspection, Inspection } from '../inspection.model';

import { InspectionService } from './inspection.service';

describe('Inspection Service', () => {
  let service: InspectionService;
  let httpMock: HttpTestingController;
  let elemDefault: IInspection;
  let expectedResult: IInspection | IInspection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InspectionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomInsp: 'AAAAAAA',
      typeInsp: TypeInspection.IA,
      latitude: 0,
      longitude: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Inspection', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Inspection()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Inspection', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomInsp: 'BBBBBB',
          typeInsp: 'BBBBBB',
          latitude: 1,
          longitude: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Inspection', () => {
      const patchObject = Object.assign(
        {
          latitude: 1,
          longitude: 1,
        },
        new Inspection()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Inspection', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomInsp: 'BBBBBB',
          typeInsp: 'BBBBBB',
          latitude: 1,
          longitude: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Inspection', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInspectionToCollectionIfMissing', () => {
      it('should add a Inspection to an empty array', () => {
        const inspection: IInspection = { id: 123 };
        expectedResult = service.addInspectionToCollectionIfMissing([], inspection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspection);
      });

      it('should not add a Inspection to an array that contains it', () => {
        const inspection: IInspection = { id: 123 };
        const inspectionCollection: IInspection[] = [
          {
            ...inspection,
          },
          { id: 456 },
        ];
        expectedResult = service.addInspectionToCollectionIfMissing(inspectionCollection, inspection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Inspection to an array that doesn't contain it", () => {
        const inspection: IInspection = { id: 123 };
        const inspectionCollection: IInspection[] = [{ id: 456 }];
        expectedResult = service.addInspectionToCollectionIfMissing(inspectionCollection, inspection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspection);
      });

      it('should add only unique Inspection to an array', () => {
        const inspectionArray: IInspection[] = [{ id: 123 }, { id: 456 }, { id: 69074 }];
        const inspectionCollection: IInspection[] = [{ id: 123 }];
        expectedResult = service.addInspectionToCollectionIfMissing(inspectionCollection, ...inspectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inspection: IInspection = { id: 123 };
        const inspection2: IInspection = { id: 456 };
        expectedResult = service.addInspectionToCollectionIfMissing([], inspection, inspection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inspection);
        expect(expectedResult).toContain(inspection2);
      });

      it('should accept null and undefined values', () => {
        const inspection: IInspection = { id: 123 };
        expectedResult = service.addInspectionToCollectionIfMissing([], null, inspection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inspection);
      });

      it('should return initial array if no Inspection is added', () => {
        const inspectionCollection: IInspection[] = [{ id: 123 }];
        expectedResult = service.addInspectionToCollectionIfMissing(inspectionCollection, undefined, null);
        expect(expectedResult).toEqual(inspectionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
