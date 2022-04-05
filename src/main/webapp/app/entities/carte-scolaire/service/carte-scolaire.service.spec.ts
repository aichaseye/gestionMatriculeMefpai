import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICarteScolaire, CarteScolaire } from '../carte-scolaire.model';

import { CarteScolaireService } from './carte-scolaire.service';

describe('CarteScolaire Service', () => {
  let service: CarteScolaireService;
  let httpMock: HttpTestingController;
  let elemDefault: ICarteScolaire;
  let expectedResult: ICarteScolaire | ICarteScolaire[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CarteScolaireService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      longuer: 0,
      largeur: 0,
      dureeValidite: 0,
      dateCreation: currentDate,
      dateFin: currentDate,
      matriculeCart: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateCreation: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CarteScolaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateCreation: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.create(new CarteScolaire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CarteScolaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          longuer: 1,
          largeur: 1,
          dureeValidite: 1,
          dateCreation: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
          matriculeCart: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CarteScolaire', () => {
      const patchObject = Object.assign(
        {
          longuer: 1,
          largeur: 1,
          matriculeCart: 'BBBBBB',
        },
        new CarteScolaire()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CarteScolaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          longuer: 1,
          largeur: 1,
          dureeValidite: 1,
          dateCreation: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
          matriculeCart: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CarteScolaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCarteScolaireToCollectionIfMissing', () => {
      it('should add a CarteScolaire to an empty array', () => {
        const carteScolaire: ICarteScolaire = { id: 123 };
        expectedResult = service.addCarteScolaireToCollectionIfMissing([], carteScolaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carteScolaire);
      });

      it('should not add a CarteScolaire to an array that contains it', () => {
        const carteScolaire: ICarteScolaire = { id: 123 };
        const carteScolaireCollection: ICarteScolaire[] = [
          {
            ...carteScolaire,
          },
          { id: 456 },
        ];
        expectedResult = service.addCarteScolaireToCollectionIfMissing(carteScolaireCollection, carteScolaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CarteScolaire to an array that doesn't contain it", () => {
        const carteScolaire: ICarteScolaire = { id: 123 };
        const carteScolaireCollection: ICarteScolaire[] = [{ id: 456 }];
        expectedResult = service.addCarteScolaireToCollectionIfMissing(carteScolaireCollection, carteScolaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carteScolaire);
      });

      it('should add only unique CarteScolaire to an array', () => {
        const carteScolaireArray: ICarteScolaire[] = [{ id: 123 }, { id: 456 }, { id: 68340 }];
        const carteScolaireCollection: ICarteScolaire[] = [{ id: 123 }];
        expectedResult = service.addCarteScolaireToCollectionIfMissing(carteScolaireCollection, ...carteScolaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const carteScolaire: ICarteScolaire = { id: 123 };
        const carteScolaire2: ICarteScolaire = { id: 456 };
        expectedResult = service.addCarteScolaireToCollectionIfMissing([], carteScolaire, carteScolaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(carteScolaire);
        expect(expectedResult).toContain(carteScolaire2);
      });

      it('should accept null and undefined values', () => {
        const carteScolaire: ICarteScolaire = { id: 123 };
        expectedResult = service.addCarteScolaireToCollectionIfMissing([], null, carteScolaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(carteScolaire);
      });

      it('should return initial array if no CarteScolaire is added', () => {
        const carteScolaireCollection: ICarteScolaire[] = [{ id: 123 }];
        expectedResult = service.addCarteScolaireToCollectionIfMissing(carteScolaireCollection, undefined, null);
        expect(expectedResult).toEqual(carteScolaireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
