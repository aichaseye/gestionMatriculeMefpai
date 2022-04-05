import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IReleveNote, ReleveNote } from '../releve-note.model';
import { ReleveNoteService } from '../service/releve-note.service';

import { ReleveNoteRoutingResolveService } from './releve-note-routing-resolve.service';

describe('ReleveNote routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ReleveNoteRoutingResolveService;
  let service: ReleveNoteService;
  let resultReleveNote: IReleveNote | undefined;

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
    routingResolveService = TestBed.inject(ReleveNoteRoutingResolveService);
    service = TestBed.inject(ReleveNoteService);
    resultReleveNote = undefined;
  });

  describe('resolve', () => {
    it('should return IReleveNote returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReleveNote = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReleveNote).toEqual({ id: 123 });
    });

    it('should return new IReleveNote if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReleveNote = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReleveNote).toEqual(new ReleveNote());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ReleveNote })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReleveNote = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReleveNote).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
