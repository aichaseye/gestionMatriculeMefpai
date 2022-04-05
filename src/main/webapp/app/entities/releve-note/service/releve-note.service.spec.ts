import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Etat } from 'app/entities/enumerations/etat.model';
import { NomSemestre } from 'app/entities/enumerations/nom-semestre.model';
import { IReleveNote, ReleveNote } from '../releve-note.model';

import { ReleveNoteService } from './releve-note.service';

describe('ReleveNote Service', () => {
  let service: ReleveNoteService;
  let httpMock: HttpTestingController;
  let elemDefault: IReleveNote;
  let expectedResult: IReleveNote | IReleveNote[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReleveNoteService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      annee: currentDate,
      etat: Etat.REDOUBLANT,
      apreciation: 'AAAAAAA',
      moyenne: 0,
      moyenneGenerale: 0,
      rang: 'AAAAAAA',
      noteConduite: 0,
      nomSemestre: NomSemestre.Semestre1,
      matriculeRel: 'AAAAAAA',
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

    it('should create a ReleveNote', () => {
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

      service.create(new ReleveNote()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReleveNote', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          annee: currentDate.format(DATE_FORMAT),
          etat: 'BBBBBB',
          apreciation: 'BBBBBB',
          moyenne: 1,
          moyenneGenerale: 1,
          rang: 'BBBBBB',
          noteConduite: 1,
          nomSemestre: 'BBBBBB',
          matriculeRel: 'BBBBBB',
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

    it('should partial update a ReleveNote', () => {
      const patchObject = Object.assign(
        {
          annee: currentDate.format(DATE_FORMAT),
          moyenne: 1,
          matriculeRel: 'BBBBBB',
        },
        new ReleveNote()
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

    it('should return a list of ReleveNote', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          annee: currentDate.format(DATE_FORMAT),
          etat: 'BBBBBB',
          apreciation: 'BBBBBB',
          moyenne: 1,
          moyenneGenerale: 1,
          rang: 'BBBBBB',
          noteConduite: 1,
          nomSemestre: 'BBBBBB',
          matriculeRel: 'BBBBBB',
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

    it('should delete a ReleveNote', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReleveNoteToCollectionIfMissing', () => {
      it('should add a ReleveNote to an empty array', () => {
        const releveNote: IReleveNote = { id: 123 };
        expectedResult = service.addReleveNoteToCollectionIfMissing([], releveNote);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(releveNote);
      });

      it('should not add a ReleveNote to an array that contains it', () => {
        const releveNote: IReleveNote = { id: 123 };
        const releveNoteCollection: IReleveNote[] = [
          {
            ...releveNote,
          },
          { id: 456 },
        ];
        expectedResult = service.addReleveNoteToCollectionIfMissing(releveNoteCollection, releveNote);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReleveNote to an array that doesn't contain it", () => {
        const releveNote: IReleveNote = { id: 123 };
        const releveNoteCollection: IReleveNote[] = [{ id: 456 }];
        expectedResult = service.addReleveNoteToCollectionIfMissing(releveNoteCollection, releveNote);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(releveNote);
      });

      it('should add only unique ReleveNote to an array', () => {
        const releveNoteArray: IReleveNote[] = [{ id: 123 }, { id: 456 }, { id: 79958 }];
        const releveNoteCollection: IReleveNote[] = [{ id: 123 }];
        expectedResult = service.addReleveNoteToCollectionIfMissing(releveNoteCollection, ...releveNoteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const releveNote: IReleveNote = { id: 123 };
        const releveNote2: IReleveNote = { id: 456 };
        expectedResult = service.addReleveNoteToCollectionIfMissing([], releveNote, releveNote2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(releveNote);
        expect(expectedResult).toContain(releveNote2);
      });

      it('should accept null and undefined values', () => {
        const releveNote: IReleveNote = { id: 123 };
        expectedResult = service.addReleveNoteToCollectionIfMissing([], null, releveNote, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(releveNote);
      });

      it('should return initial array if no ReleveNote is added', () => {
        const releveNoteCollection: IReleveNote[] = [{ id: 123 }];
        expectedResult = service.addReleveNoteToCollectionIfMissing(releveNoteCollection, undefined, null);
        expect(expectedResult).toEqual(releveNoteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
