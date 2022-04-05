import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICarteScolaire, CarteScolaire } from '../carte-scolaire.model';
import { CarteScolaireService } from '../service/carte-scolaire.service';

import { CarteScolaireRoutingResolveService } from './carte-scolaire-routing-resolve.service';

describe('CarteScolaire routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CarteScolaireRoutingResolveService;
  let service: CarteScolaireService;
  let resultCarteScolaire: ICarteScolaire | undefined;

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
    routingResolveService = TestBed.inject(CarteScolaireRoutingResolveService);
    service = TestBed.inject(CarteScolaireService);
    resultCarteScolaire = undefined;
  });

  describe('resolve', () => {
    it('should return ICarteScolaire returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCarteScolaire = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCarteScolaire).toEqual({ id: 123 });
    });

    it('should return new ICarteScolaire if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCarteScolaire = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCarteScolaire).toEqual(new CarteScolaire());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CarteScolaire })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCarteScolaire = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCarteScolaire).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
