import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { NomDiplome } from 'app/entities/enumerations/nom-diplome.model';
import { IDiplome, Diplome } from '../diplome.model';

import { DiplomeService } from './diplome.service';

describe('Diplome Service', () => {
  let service: DiplomeService;
  let httpMock: HttpTestingController;
  let elemDefault: IDiplome;
  let expectedResult: IDiplome | IDiplome[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DiplomeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nom: NomDiplome.CAP,
      matriculeDiplome: 'AAAAAAA',
      annee: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Diplome', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.create(new Diplome()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Diplome', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          matriculeDiplome: 'BBBBBB',
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Diplome', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
          matriculeDiplome: 'BBBBBB',
          annee: currentDate.format(DATE_FORMAT),
        },
        new Diplome()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Diplome', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          matriculeDiplome: 'BBBBBB',
          annee: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          annee: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Diplome', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDiplomeToCollectionIfMissing', () => {
      it('should add a Diplome to an empty array', () => {
        const diplome: IDiplome = { id: 123 };
        expectedResult = service.addDiplomeToCollectionIfMissing([], diplome);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(diplome);
      });

      it('should not add a Diplome to an array that contains it', () => {
        const diplome: IDiplome = { id: 123 };
        const diplomeCollection: IDiplome[] = [
          {
            ...diplome,
          },
          { id: 456 },
        ];
        expectedResult = service.addDiplomeToCollectionIfMissing(diplomeCollection, diplome);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Diplome to an array that doesn't contain it", () => {
        const diplome: IDiplome = { id: 123 };
        const diplomeCollection: IDiplome[] = [{ id: 456 }];
        expectedResult = service.addDiplomeToCollectionIfMissing(diplomeCollection, diplome);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(diplome);
      });

      it('should add only unique Diplome to an array', () => {
        const diplomeArray: IDiplome[] = [{ id: 123 }, { id: 456 }, { id: 11561 }];
        const diplomeCollection: IDiplome[] = [{ id: 123 }];
        expectedResult = service.addDiplomeToCollectionIfMissing(diplomeCollection, ...diplomeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const diplome: IDiplome = { id: 123 };
        const diplome2: IDiplome = { id: 456 };
        expectedResult = service.addDiplomeToCollectionIfMissing([], diplome, diplome2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(diplome);
        expect(expectedResult).toContain(diplome2);
      });

      it('should accept null and undefined values', () => {
        const diplome: IDiplome = { id: 123 };
        expectedResult = service.addDiplomeToCollectionIfMissing([], null, diplome, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(diplome);
      });

      it('should return initial array if no Diplome is added', () => {
        const diplomeCollection: IDiplome[] = [{ id: 123 }];
        expectedResult = service.addDiplomeToCollectionIfMissing(diplomeCollection, undefined, null);
        expect(expectedResult).toEqual(diplomeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
