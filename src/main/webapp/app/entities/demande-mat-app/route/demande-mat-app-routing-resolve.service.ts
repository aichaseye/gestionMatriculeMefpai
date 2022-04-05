import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemandeMatApp, DemandeMatApp } from '../demande-mat-app.model';
import { DemandeMatAppService } from '../service/demande-mat-app.service';

@Injectable({ providedIn: 'root' })
export class DemandeMatAppRoutingResolveService implements Resolve<IDemandeMatApp> {
  constructor(protected service: DemandeMatAppService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDemandeMatApp> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((demandeMatApp: HttpResponse<DemandeMatApp>) => {
          if (demandeMatApp.body) {
            return of(demandeMatApp.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DemandeMatApp());
  }
}
