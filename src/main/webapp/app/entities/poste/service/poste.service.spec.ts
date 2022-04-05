import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { NomPoste } from 'app/entities/enumerations/nom-poste.model';
import { IPoste, Poste } from '../poste.model';

import { PosteService } from './poste.service';

describe('Poste Service', () => {
  let service: PosteService;
  let httpMock: HttpTestingController;
  let elemDefault: IPoste;
  let expectedResult: IPoste | IPoste[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PosteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomPoste: NomPoste.Comptable_matiere,
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

    it('should create a Poste', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Poste()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Poste', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomPoste: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Poste', () => {
      const patchObject = Object.assign(
        {
          nomPoste: 'BBBBBB',
        },
        new Poste()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Poste', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomPoste: 'BBBBBB',
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

    it('should delete a Poste', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPosteToCollectionIfMissing', () => {
      it('should add a Poste to an empty array', () => {
        const poste: IPoste = { id: 123 };
        expectedResult = service.addPosteToCollectionIfMissing([], poste);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(poste);
      });

      it('should not add a Poste to an array that contains it', () => {
        const poste: IPoste = { id: 123 };
        const posteCollection: IPoste[] = [
          {
            ...poste,
          },
          { id: 456 },
        ];
        expectedResult = service.addPosteToCollectionIfMissing(posteCollection, poste);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Poste to an array that doesn't contain it", () => {
        const poste: IPoste = { id: 123 };
        const posteCollection: IPoste[] = [{ id: 456 }];
        expectedResult = service.addPosteToCollectionIfMissing(posteCollection, poste);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(poste);
      });

      it('should add only unique Poste to an array', () => {
        const posteArray: IPoste[] = [{ id: 123 }, { id: 456 }, { id: 38177 }];
        const posteCollection: IPoste[] = [{ id: 123 }];
        expectedResult = service.addPosteToCollectionIfMissing(posteCollection, ...posteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const poste: IPoste = { id: 123 };
        const poste2: IPoste = { id: 456 };
        expectedResult = service.addPosteToCollectionIfMissing([], poste, poste2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(poste);
        expect(expectedResult).toContain(poste2);
      });

      it('should accept null and undefined values', () => {
        const poste: IPoste = { id: 123 };
        expectedResult = service.addPosteToCollectionIfMissing([], null, poste, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(poste);
      });

      it('should return initial array if no Poste is added', () => {
        const posteCollection: IPoste[] = [{ id: 123 }];
        expectedResult = service.addPosteToCollectionIfMissing(posteCollection, undefined, null);
        expect(expectedResult).toEqual(posteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
