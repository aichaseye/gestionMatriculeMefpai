import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApprenant, Apprenant } from '../apprenant.model';
import { ApprenantService } from '../service/apprenant.service';

@Injectable({ providedIn: 'root' })
export class ApprenantRoutingResolveService implements Resolve<IApprenant> {
  constructor(protected service: ApprenantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApprenant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((apprenant: HttpResponse<Apprenant>) => {
          if (apprenant.body) {
            return of(apprenant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Apprenant());
  }
}
