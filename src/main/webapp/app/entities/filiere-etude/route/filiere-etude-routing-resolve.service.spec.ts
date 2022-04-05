import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFiliereEtude, FiliereEtude } from '../filiere-etude.model';
import { FiliereEtudeService } from '../service/filiere-etude.service';

import { FiliereEtudeRoutingResolveService } from './filiere-etude-routing-resolve.service';

describe('FiliereEtude routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FiliereEtudeRoutingResolveService;
  let service: FiliereEtudeService;
  let resultFiliereEtude: IFiliereEtude | undefined;

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
    routingResolveService = TestBed.inject(FiliereEtudeRoutingResolveService);
    service = TestBed.inject(FiliereEtudeService);
    resultFiliereEtude = undefined;
  });

  describe('resolve', () => {
    it('should return IFiliereEtude returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFiliereEtude = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFiliereEtude).toEqual({ id: 123 });
    });

    it('should return new IFiliereEtude if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFiliereEtude = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFiliereEtude).toEqual(new FiliereEtude());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FiliereEtude })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFiliereEtude = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFiliereEtude).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
