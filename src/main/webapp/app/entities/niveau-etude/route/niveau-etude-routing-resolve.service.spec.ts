import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { INiveauEtude, NiveauEtude } from '../niveau-etude.model';
import { NiveauEtudeService } from '../service/niveau-etude.service';

import { NiveauEtudeRoutingResolveService } from './niveau-etude-routing-resolve.service';

describe('NiveauEtude routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: NiveauEtudeRoutingResolveService;
  let service: NiveauEtudeService;
  let resultNiveauEtude: INiveauEtude | undefined;

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
    routingResolveService = TestBed.inject(NiveauEtudeRoutingResolveService);
    service = TestBed.inject(NiveauEtudeService);
    resultNiveauEtude = undefined;
  });

  describe('resolve', () => {
    it('should return INiveauEtude returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNiveauEtude = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNiveauEtude).toEqual({ id: 123 });
    });

    it('should return new INiveauEtude if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNiveauEtude = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultNiveauEtude).toEqual(new NiveauEtude());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as NiveauEtude })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNiveauEtude = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNiveauEtude).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
