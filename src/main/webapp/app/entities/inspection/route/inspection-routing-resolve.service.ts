import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInspection, Inspection } from '../inspection.model';
import { InspectionService } from '../service/inspection.service';

@Injectable({ providedIn: 'root' })
export class InspectionRoutingResolveService implements Resolve<IInspection> {
  constructor(protected service: InspectionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInspection> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inspection: HttpResponse<Inspection>) => {
          if (inspection.body) {
            return of(inspection.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Inspection());
  }
}
