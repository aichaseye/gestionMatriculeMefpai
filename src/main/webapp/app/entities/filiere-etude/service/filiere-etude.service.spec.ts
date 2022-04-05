import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Filiere } from 'app/entities/enumerations/filiere.model';
import { IFiliereEtude, FiliereEtude } from '../filiere-etude.model';

import { FiliereEtudeService } from './filiere-etude.service';

describe('FiliereEtude Service', () => {
  let service: FiliereEtudeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFiliereEtude;
  let expectedResult: IFiliereEtude | IFiliereEtude[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FiliereEtudeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      filiere: Filiere.Agricultre,
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

    it('should create a FiliereEtude', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FiliereEtude()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FiliereEtude', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          filiere: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FiliereEtude', () => {
      const patchObject = Object.assign({}, new FiliereEtude());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FiliereEtude', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          filiere: 'BBBBBB',
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

    it('should delete a FiliereEtude', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFiliereEtudeToCollectionIfMissing', () => {
      it('should add a FiliereEtude to an empty array', () => {
        const filiereEtude: IFiliereEtude = { id: 123 };
        expectedResult = service.addFiliereEtudeToCollectionIfMissing([], filiereEtude);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(filiereEtude);
      });

      it('should not add a FiliereEtude to an array that contains it', () => {
        const filiereEtude: IFiliereEtude = { id: 123 };
        const filiereEtudeCollection: IFiliereEtude[] = [
          {
            ...filiereEtude,
          },
          { id: 456 },
        ];
        expectedResult = service.addFiliereEtudeToCollectionIfMissing(filiereEtudeCollection, filiereEtude);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FiliereEtude to an array that doesn't contain it", () => {
        const filiereEtude: IFiliereEtude = { id: 123 };
        const filiereEtudeCollection: IFiliereEtude[] = [{ id: 456 }];
        expectedResult = service.addFiliereEtudeToCollectionIfMissing(filiereEtudeCollection, filiereEtude);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(filiereEtude);
      });

      it('should add only unique FiliereEtude to an array', () => {
        const filiereEtudeArray: IFiliereEtude[] = [{ id: 123 }, { id: 456 }, { id: 80971 }];
        const filiereEtudeCollection: IFiliereEtude[] = [{ id: 123 }];
        expectedResult = service.addFiliereEtudeToCollectionIfMissing(filiereEtudeCollection, ...filiereEtudeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const filiereEtude: IFiliereEtude = { id: 123 };
        const filiereEtude2: IFiliereEtude = { id: 456 };
        expectedResult = service.addFiliereEtudeToCollectionIfMissing([], filiereEtude, filiereEtude2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(filiereEtude);
        expect(expectedResult).toContain(filiereEtude2);
      });

      it('should accept null and undefined values', () => {
        const filiereEtude: IFiliereEtude = { id: 123 };
        expectedResult = service.addFiliereEtudeToCollectionIfMissing([], null, filiereEtude, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(filiereEtude);
      });

      it('should return initial array if no FiliereEtude is added', () => {
        const filiereEtudeCollection: IFiliereEtude[] = [{ id: 123 }];
        expectedResult = service.addFiliereEtudeToCollectionIfMissing(filiereEtudeCollection, undefined, null);
        expect(expectedResult).toEqual(filiereEtudeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
