import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDemandeMatApp, DemandeMatApp } from '../demande-mat-app.model';

import { DemandeMatAppService } from './demande-mat-app.service';

describe('DemandeMatApp Service', () => {
  let service: DemandeMatAppService;
  let httpMock: HttpTestingController;
  let elemDefault: IDemandeMatApp;
  let expectedResult: IDemandeMatApp | IDemandeMatApp[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DemandeMatAppService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      objet: 'AAAAAAA',
      description: 'AAAAAAA',
      dateDemande: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateDemande: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DemandeMatApp', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateDemande: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDemande: currentDate,
        },
        returnedFromService
      );

      service.create(new DemandeMatApp()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DemandeMatApp', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          objet: 'BBBBBB',
          description: 'BBBBBB',
          dateDemande: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDemande: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DemandeMatApp', () => {
      const patchObject = Object.assign(
        {
          objet: 'BBBBBB',
          description: 'BBBBBB',
        },
        new DemandeMatApp()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateDemande: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DemandeMatApp', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          objet: 'BBBBBB',
          description: 'BBBBBB',
          dateDemande: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDemande: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DemandeMatApp', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDemandeMatAppToCollectionIfMissing', () => {
      it('should add a DemandeMatApp to an empty array', () => {
        const demandeMatApp: IDemandeMatApp = { id: 123 };
        expectedResult = service.addDemandeMatAppToCollectionIfMissing([], demandeMatApp);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeMatApp);
      });

      it('should not add a DemandeMatApp to an array that contains it', () => {
        const demandeMatApp: IDemandeMatApp = { id: 123 };
        const demandeMatAppCollection: IDemandeMatApp[] = [
          {
            ...demandeMatApp,
          },
          { id: 456 },
        ];
        expectedResult = service.addDemandeMatAppToCollectionIfMissing(demandeMatAppCollection, demandeMatApp);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DemandeMatApp to an array that doesn't contain it", () => {
        const demandeMatApp: IDemandeMatApp = { id: 123 };
        const demandeMatAppCollection: IDemandeMatApp[] = [{ id: 456 }];
        expectedResult = service.addDemandeMatAppToCollectionIfMissing(demandeMatAppCollection, demandeMatApp);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeMatApp);
      });

      it('should add only unique DemandeMatApp to an array', () => {
        const demandeMatAppArray: IDemandeMatApp[] = [{ id: 123 }, { id: 456 }, { id: 23878 }];
        const demandeMatAppCollection: IDemandeMatApp[] = [{ id: 123 }];
        expectedResult = service.addDemandeMatAppToCollectionIfMissing(demandeMatAppCollection, ...demandeMatAppArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const demandeMatApp: IDemandeMatApp = { id: 123 };
        const demandeMatApp2: IDemandeMatApp = { id: 456 };
        expectedResult = service.addDemandeMatAppToCollectionIfMissing([], demandeMatApp, demandeMatApp2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeMatApp);
        expect(expectedResult).toContain(demandeMatApp2);
      });

      it('should accept null and undefined values', () => {
        const demandeMatApp: IDemandeMatApp = { id: 123 };
        expectedResult = service.addDemandeMatAppToCollectionIfMissing([], null, demandeMatApp, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeMatApp);
      });

      it('should return initial array if no DemandeMatApp is added', () => {
        const demandeMatAppCollection: IDemandeMatApp[] = [{ id: 123 }];
        expectedResult = service.addDemandeMatAppToCollectionIfMissing(demandeMatAppCollection, undefined, null);
        expect(expectedResult).toEqual(demandeMatAppCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
