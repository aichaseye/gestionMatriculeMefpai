import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INoteProgramme, NoteProgramme } from '../note-programme.model';

import { NoteProgrammeService } from './note-programme.service';

describe('NoteProgramme Service', () => {
  let service: NoteProgrammeService;
  let httpMock: HttpTestingController;
  let elemDefault: INoteProgramme;
  let expectedResult: INoteProgramme | INoteProgramme[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NoteProgrammeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomProg: 'AAAAAAA',
      coef: 0,
      note: 0,
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

    it('should create a NoteProgramme', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new NoteProgramme()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NoteProgramme', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomProg: 'BBBBBB',
          coef: 1,
          note: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NoteProgramme', () => {
      const patchObject = Object.assign(
        {
          nomProg: 'BBBBBB',
        },
        new NoteProgramme()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NoteProgramme', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomProg: 'BBBBBB',
          coef: 1,
          note: 1,
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

    it('should delete a NoteProgramme', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNoteProgrammeToCollectionIfMissing', () => {
      it('should add a NoteProgramme to an empty array', () => {
        const noteProgramme: INoteProgramme = { id: 123 };
        expectedResult = service.addNoteProgrammeToCollectionIfMissing([], noteProgramme);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(noteProgramme);
      });

      it('should not add a NoteProgramme to an array that contains it', () => {
        const noteProgramme: INoteProgramme = { id: 123 };
        const noteProgrammeCollection: INoteProgramme[] = [
          {
            ...noteProgramme,
          },
          { id: 456 },
        ];
        expectedResult = service.addNoteProgrammeToCollectionIfMissing(noteProgrammeCollection, noteProgramme);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NoteProgramme to an array that doesn't contain it", () => {
        const noteProgramme: INoteProgramme = { id: 123 };
        const noteProgrammeCollection: INoteProgramme[] = [{ id: 456 }];
        expectedResult = service.addNoteProgrammeToCollectionIfMissing(noteProgrammeCollection, noteProgramme);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(noteProgramme);
      });

      it('should add only unique NoteProgramme to an array', () => {
        const noteProgrammeArray: INoteProgramme[] = [{ id: 123 }, { id: 456 }, { id: 77931 }];
        const noteProgrammeCollection: INoteProgramme[] = [{ id: 123 }];
        expectedResult = service.addNoteProgrammeToCollectionIfMissing(noteProgrammeCollection, ...noteProgrammeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const noteProgramme: INoteProgramme = { id: 123 };
        const noteProgramme2: INoteProgramme = { id: 456 };
        expectedResult = service.addNoteProgrammeToCollectionIfMissing([], noteProgramme, noteProgramme2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(noteProgramme);
        expect(expectedResult).toContain(noteProgramme2);
      });

      it('should accept null and undefined values', () => {
        const noteProgramme: INoteProgramme = { id: 123 };
        expectedResult = service.addNoteProgrammeToCollectionIfMissing([], null, noteProgramme, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(noteProgramme);
      });

      it('should return initial array if no NoteProgramme is added', () => {
        const noteProgrammeCollection: INoteProgramme[] = [{ id: 123 }];
        expectedResult = service.addNoteProgrammeToCollectionIfMissing(noteProgrammeCollection, undefined, null);
        expect(expectedResult).toEqual(noteProgrammeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
