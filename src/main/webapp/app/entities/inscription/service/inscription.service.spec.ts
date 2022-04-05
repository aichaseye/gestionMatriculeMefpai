import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInscription, Inscription } from '../inscription.model';

import { InscriptionService } from './inscription.service';

describe('Inscription Service', () => {
  let service: InscriptionService;
  let httpMock: HttpTestingController;
  let elemDefault: IInscription;
  let expectedResult: IInscription | IInscription[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InscriptionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      anneeIns: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          anneeIns: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Inscription', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          anneeIns: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          anneeIns: currentDate,
        },
        returnedFromService
      );

      service.create(new Inscription()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Inscription', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          anneeIns: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          anneeIns: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Inscription', () => {
      const patchObject = Object.assign({}, new Inscription());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          anneeIns: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Inscription', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          anneeIns: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          anneeIns: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Inscription', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInscriptionToCollectionIfMissing', () => {
      it('should add a Inscription to an empty array', () => {
        const inscription: IInscription = { id: 123 };
        expectedResult = service.addInscriptionToCollectionIfMissing([], inscription);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscription);
      });

      it('should not add a Inscription to an array that contains it', () => {
        const inscription: IInscription = { id: 123 };
        const inscriptionCollection: IInscription[] = [
          {
            ...inscription,
          },
          { id: 456 },
        ];
        expectedResult = service.addInscriptionToCollectionIfMissing(inscriptionCollection, inscription);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Inscription to an array that doesn't contain it", () => {
        const inscription: IInscription = { id: 123 };
        const inscriptionCollection: IInscription[] = [{ id: 456 }];
        expectedResult = service.addInscriptionToCollectionIfMissing(inscriptionCollection, inscription);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscription);
      });

      it('should add only unique Inscription to an array', () => {
        const inscriptionArray: IInscription[] = [{ id: 123 }, { id: 456 }, { id: 25719 }];
        const inscriptionCollection: IInscription[] = [{ id: 123 }];
        expectedResult = service.addInscriptionToCollectionIfMissing(inscriptionCollection, ...inscriptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inscription: IInscription = { id: 123 };
        const inscription2: IInscription = { id: 456 };
        expectedResult = service.addInscriptionToCollectionIfMissing([], inscription, inscription2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscription);
        expect(expectedResult).toContain(inscription2);
      });

      it('should accept null and undefined values', () => {
        const inscription: IInscription = { id: 123 };
        expectedResult = service.addInscriptionToCollectionIfMissing([], null, inscription, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscription);
      });

      it('should return initial array if no Inscription is added', () => {
        const inscriptionCollection: IInscription[] = [{ id: 123 }];
        expectedResult = service.addInscriptionToCollectionIfMissing(inscriptionCollection, undefined, null);
        expect(expectedResult).toEqual(inscriptionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
