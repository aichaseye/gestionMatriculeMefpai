import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommune, Commune } from '../commune.model';

import { CommuneService } from './commune.service';

describe('Commune Service', () => {
  let service: CommuneService;
  let httpMock: HttpTestingController;
  let elemDefault: ICommune;
  let expectedResult: ICommune | ICommune[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommuneService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomCom: 'AAAAAAA',
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

    it('should create a Commune', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Commune()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Commune', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomCom: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Commune', () => {
      const patchObject = Object.assign({}, new Commune());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Commune', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomCom: 'BBBBBB',
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

    it('should delete a Commune', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCommuneToCollectionIfMissing', () => {
      it('should add a Commune to an empty array', () => {
        const commune: ICommune = { id: 123 };
        expectedResult = service.addCommuneToCollectionIfMissing([], commune);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commune);
      });

      it('should not add a Commune to an array that contains it', () => {
        const commune: ICommune = { id: 123 };
        const communeCollection: ICommune[] = [
          {
            ...commune,
          },
          { id: 456 },
        ];
        expectedResult = service.addCommuneToCollectionIfMissing(communeCollection, commune);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Commune to an array that doesn't contain it", () => {
        const commune: ICommune = { id: 123 };
        const communeCollection: ICommune[] = [{ id: 456 }];
        expectedResult = service.addCommuneToCollectionIfMissing(communeCollection, commune);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commune);
      });

      it('should add only unique Commune to an array', () => {
        const communeArray: ICommune[] = [{ id: 123 }, { id: 456 }, { id: 84051 }];
        const communeCollection: ICommune[] = [{ id: 123 }];
        expectedResult = service.addCommuneToCollectionIfMissing(communeCollection, ...communeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commune: ICommune = { id: 123 };
        const commune2: ICommune = { id: 456 };
        expectedResult = service.addCommuneToCollectionIfMissing([], commune, commune2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commune);
        expect(expectedResult).toContain(commune2);
      });

      it('should accept null and undefined values', () => {
        const commune: ICommune = { id: 123 };
        expectedResult = service.addCommuneToCollectionIfMissing([], null, commune, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commune);
      });

      it('should return initial array if no Commune is added', () => {
        const communeCollection: ICommune[] = [{ id: 123 }];
        expectedResult = service.addCommuneToCollectionIfMissing(communeCollection, undefined, null);
        expect(expectedResult).toEqual(communeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
