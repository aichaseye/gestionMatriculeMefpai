import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDemandeMatEtab, DemandeMatEtab } from '../demande-mat-etab.model';
import { DemandeMatEtabService } from '../service/demande-mat-etab.service';

import { DemandeMatEtabRoutingResolveService } from './demande-mat-etab-routing-resolve.service';

describe('DemandeMatEtab routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DemandeMatEtabRoutingResolveService;
  let service: DemandeMatEtabService;
  let resultDemandeMatEtab: IDemandeMatEtab | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(DemandeMatEtabRoutingResolveService);
    service = TestBed.inject(DemandeMatEtabService);
    resultDemandeMatEtab = undefined;
  });

  describe('resolve', () => {
    it('should return IDemandeMatEtab returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeMatEtab = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeMatEtab).toEqual({ id: 123 });
    });

    it('should return new IDemandeMatEtab if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeMatEtab = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDemandeMatEtab).toEqual(new DemandeMatEtab());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DemandeMatEtab })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDemandeMatEtab = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDemandeMatEtab).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
