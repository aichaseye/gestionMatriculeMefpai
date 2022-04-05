import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMatiere, Matiere } from '../matiere.model';

import { MatiereService } from './matiere.service';

describe('Matiere Service', () => {
  let service: MatiereService;
  let httpMock: HttpTestingController;
  let elemDefault: IMatiere;
  let expectedResult: IMatiere | IMatiere[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MatiereService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomMatiere: 'AAAAAAA',
      reference: 'AAAAAAA',
      typeMatiere: 'AAAAAAA',
      matriculeMatiere: 'AAAAAAA',
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

    it('should create a Matiere', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Matiere()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Matiere', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomMatiere: 'BBBBBB',
          reference: 'BBBBBB',
          typeMatiere: 'BBBBBB',
          matriculeMatiere: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Matiere', () => {
      const patchObject = Object.assign(
        {
          reference: 'BBBBBB',
          matriculeMatiere: 'BBBBBB',
        },
        new Matiere()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Matiere', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomMatiere: 'BBBBBB',
          reference: 'BBBBBB',
          typeMatiere: 'BBBBBB',
          matriculeMatiere: 'BBBBBB',
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

    it('should delete a Matiere', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMatiereToCollectionIfMissing', () => {
      it('should add a Matiere to an empty array', () => {
        const matiere: IMatiere = { id: 123 };
        expectedResult = service.addMatiereToCollectionIfMissing([], matiere);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matiere);
      });

      it('should not add a Matiere to an array that contains it', () => {
        const matiere: IMatiere = { id: 123 };
        const matiereCollection: IMatiere[] = [
          {
            ...matiere,
          },
          { id: 456 },
        ];
        expectedResult = service.addMatiereToCollectionIfMissing(matiereCollection, matiere);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Matiere to an array that doesn't contain it", () => {
        const matiere: IMatiere = { id: 123 };
        const matiereCollection: IMatiere[] = [{ id: 456 }];
        expectedResult = service.addMatiereToCollectionIfMissing(matiereCollection, matiere);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matiere);
      });

      it('should add only unique Matiere to an array', () => {
        const matiereArray: IMatiere[] = [{ id: 123 }, { id: 456 }, { id: 31677 }];
        const matiereCollection: IMatiere[] = [{ id: 123 }];
        expectedResult = service.addMatiereToCollectionIfMissing(matiereCollection, ...matiereArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const matiere: IMatiere = { id: 123 };
        const matiere2: IMatiere = { id: 456 };
        expectedResult = service.addMatiereToCollectionIfMissing([], matiere, matiere2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matiere);
        expect(expectedResult).toContain(matiere2);
      });

      it('should accept null and undefined values', () => {
        const matiere: IMatiere = { id: 123 };
        expectedResult = service.addMatiereToCollectionIfMissing([], null, matiere, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matiere);
      });

      it('should return initial array if no Matiere is added', () => {
        const matiereCollection: IMatiere[] = [{ id: 123 }];
        expectedResult = service.addMatiereToCollectionIfMissing(matiereCollection, undefined, null);
        expect(expectedResult).toEqual(matiereCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
