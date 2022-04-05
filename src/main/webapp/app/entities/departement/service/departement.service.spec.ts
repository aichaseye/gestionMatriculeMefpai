import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { NomDep } from 'app/entities/enumerations/nom-dep.model';
import { IDepartement, Departement } from '../departement.model';

import { DepartementService } from './departement.service';

describe('Departement Service', () => {
  let service: DepartementService;
  let httpMock: HttpTestingController;
  let elemDefault: IDepartement;
  let expectedResult: IDepartement | IDepartement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepartementService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomDep: NomDep.Dakar,
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

    it('should create a Departement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Departement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Departement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomDep: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Departement', () => {
      const patchObject = Object.assign(
        {
          nomDep: 'BBBBBB',
        },
        new Departement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Departement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomDep: 'BBBBBB',
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

    it('should delete a Departement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepartementToCollectionIfMissing', () => {
      it('should add a Departement to an empty array', () => {
        const departement: IDepartement = { id: 123 };
        expectedResult = service.addDepartementToCollectionIfMissing([], departement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(departement);
      });

      it('should not add a Departement to an array that contains it', () => {
        const departement: IDepartement = { id: 123 };
        const departementCollection: IDepartement[] = [
          {
            ...departement,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepartementToCollectionIfMissing(departementCollection, departement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Departement to an array that doesn't contain it", () => {
        const departement: IDepartement = { id: 123 };
        const departementCollection: IDepartement[] = [{ id: 456 }];
        expectedResult = service.addDepartementToCollectionIfMissing(departementCollection, departement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(departement);
      });

      it('should add only unique Departement to an array', () => {
        const departementArray: IDepartement[] = [{ id: 123 }, { id: 456 }, { id: 60282 }];
        const departementCollection: IDepartement[] = [{ id: 123 }];
        expectedResult = service.addDepartementToCollectionIfMissing(departementCollection, ...departementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const departement: IDepartement = { id: 123 };
        const departement2: IDepartement = { id: 456 };
        expectedResult = service.addDepartementToCollectionIfMissing([], departement, departement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(departement);
        expect(expectedResult).toContain(departement2);
      });

      it('should accept null and undefined values', () => {
        const departement: IDepartement = { id: 123 };
        expectedResult = service.addDepartementToCollectionIfMissing([], null, departement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(departement);
      });

      it('should return initial array if no Departement is added', () => {
        const departementCollection: IDepartement[] = [{ id: 123 }];
        expectedResult = service.addDepartementToCollectionIfMissing(departementCollection, undefined, null);
        expect(expectedResult).toEqual(departementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
