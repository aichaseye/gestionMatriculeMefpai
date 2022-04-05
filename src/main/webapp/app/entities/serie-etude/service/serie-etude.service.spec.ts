import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Serie } from 'app/entities/enumerations/serie.model';
import { ISerieEtude, SerieEtude } from '../serie-etude.model';

import { SerieEtudeService } from './serie-etude.service';

describe('SerieEtude Service', () => {
  let service: SerieEtudeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISerieEtude;
  let expectedResult: ISerieEtude | ISerieEtude[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SerieEtudeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      serie: Serie.STEG,
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

    it('should create a SerieEtude', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SerieEtude()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SerieEtude', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          serie: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SerieEtude', () => {
      const patchObject = Object.assign({}, new SerieEtude());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SerieEtude', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          serie: 'BBBBBB',
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

    it('should delete a SerieEtude', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSerieEtudeToCollectionIfMissing', () => {
      it('should add a SerieEtude to an empty array', () => {
        const serieEtude: ISerieEtude = { id: 123 };
        expectedResult = service.addSerieEtudeToCollectionIfMissing([], serieEtude);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serieEtude);
      });

      it('should not add a SerieEtude to an array that contains it', () => {
        const serieEtude: ISerieEtude = { id: 123 };
        const serieEtudeCollection: ISerieEtude[] = [
          {
            ...serieEtude,
          },
          { id: 456 },
        ];
        expectedResult = service.addSerieEtudeToCollectionIfMissing(serieEtudeCollection, serieEtude);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SerieEtude to an array that doesn't contain it", () => {
        const serieEtude: ISerieEtude = { id: 123 };
        const serieEtudeCollection: ISerieEtude[] = [{ id: 456 }];
        expectedResult = service.addSerieEtudeToCollectionIfMissing(serieEtudeCollection, serieEtude);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serieEtude);
      });

      it('should add only unique SerieEtude to an array', () => {
        const serieEtudeArray: ISerieEtude[] = [{ id: 123 }, { id: 456 }, { id: 43418 }];
        const serieEtudeCollection: ISerieEtude[] = [{ id: 123 }];
        expectedResult = service.addSerieEtudeToCollectionIfMissing(serieEtudeCollection, ...serieEtudeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serieEtude: ISerieEtude = { id: 123 };
        const serieEtude2: ISerieEtude = { id: 456 };
        expectedResult = service.addSerieEtudeToCollectionIfMissing([], serieEtude, serieEtude2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serieEtude);
        expect(expectedResult).toContain(serieEtude2);
      });

      it('should accept null and undefined values', () => {
        const serieEtude: ISerieEtude = { id: 123 };
        expectedResult = service.addSerieEtudeToCollectionIfMissing([], null, serieEtude, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serieEtude);
      });

      it('should return initial array if no SerieEtude is added', () => {
        const serieEtudeCollection: ISerieEtude[] = [{ id: 123 }];
        expectedResult = service.addSerieEtudeToCollectionIfMissing(serieEtudeCollection, undefined, null);
        expect(expectedResult).toEqual(serieEtudeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
