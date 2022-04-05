import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeEtab } from 'app/entities/enumerations/type-etab.model';
import { StatutEtab } from 'app/entities/enumerations/statut-etab.model';
import { IEtablissement, Etablissement } from '../etablissement.model';

import { EtablissementService } from './etablissement.service';

describe('Etablissement Service', () => {
  let service: EtablissementService;
  let httpMock: HttpTestingController;
  let elemDefault: IEtablissement;
  let expectedResult: IEtablissement | IEtablissement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EtablissementService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomEtab: 'AAAAAAA',
      typeEtab: TypeEtab.LyceeTechnique,
      statut: StatutEtab.Prive,
      latitude: 0,
      longitude: 0,
      matriculeEtab: 'AAAAAAA',
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

    it('should create a Etablissement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Etablissement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Etablissement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEtab: 'BBBBBB',
          typeEtab: 'BBBBBB',
          statut: 'BBBBBB',
          latitude: 1,
          longitude: 1,
          matriculeEtab: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Etablissement', () => {
      const patchObject = Object.assign(
        {
          nomEtab: 'BBBBBB',
          typeEtab: 'BBBBBB',
          matriculeEtab: 'BBBBBB',
        },
        new Etablissement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Etablissement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEtab: 'BBBBBB',
          typeEtab: 'BBBBBB',
          statut: 'BBBBBB',
          latitude: 1,
          longitude: 1,
          matriculeEtab: 'BBBBBB',
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

    it('should delete a Etablissement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEtablissementToCollectionIfMissing', () => {
      it('should add a Etablissement to an empty array', () => {
        const etablissement: IEtablissement = { id: 123 };
        expectedResult = service.addEtablissementToCollectionIfMissing([], etablissement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etablissement);
      });

      it('should not add a Etablissement to an array that contains it', () => {
        const etablissement: IEtablissement = { id: 123 };
        const etablissementCollection: IEtablissement[] = [
          {
            ...etablissement,
          },
          { id: 456 },
        ];
        expectedResult = service.addEtablissementToCollectionIfMissing(etablissementCollection, etablissement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Etablissement to an array that doesn't contain it", () => {
        const etablissement: IEtablissement = { id: 123 };
        const etablissementCollection: IEtablissement[] = [{ id: 456 }];
        expectedResult = service.addEtablissementToCollectionIfMissing(etablissementCollection, etablissement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etablissement);
      });

      it('should add only unique Etablissement to an array', () => {
        const etablissementArray: IEtablissement[] = [{ id: 123 }, { id: 456 }, { id: 39300 }];
        const etablissementCollection: IEtablissement[] = [{ id: 123 }];
        expectedResult = service.addEtablissementToCollectionIfMissing(etablissementCollection, ...etablissementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const etablissement: IEtablissement = { id: 123 };
        const etablissement2: IEtablissement = { id: 456 };
        expectedResult = service.addEtablissementToCollectionIfMissing([], etablissement, etablissement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etablissement);
        expect(expectedResult).toContain(etablissement2);
      });

      it('should accept null and undefined values', () => {
        const etablissement: IEtablissement = { id: 123 };
        expectedResult = service.addEtablissementToCollectionIfMissing([], null, etablissement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etablissement);
      });

      it('should return initial array if no Etablissement is added', () => {
        const etablissementCollection: IEtablissement[] = [{ id: 123 }];
        expectedResult = service.addEtablissementToCollectionIfMissing(etablissementCollection, undefined, null);
        expect(expectedResult).toEqual(etablissementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
