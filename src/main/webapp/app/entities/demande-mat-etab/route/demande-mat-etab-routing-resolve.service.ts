import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemandeMatEtab, DemandeMatEtab } from '../demande-mat-etab.model';
import { DemandeMatEtabService } from '../service/demande-mat-etab.service';

@Injectable({ providedIn: 'root' })
export class DemandeMatEtabRoutingResolveService implements Resolve<IDemandeMatEtab> {
  constructor(protected service: DemandeMatEtabService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDemandeMatEtab> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((demandeMatEtab: HttpResponse<DemandeMatEtab>) => {
          if (demandeMatEtab.body) {
            return of(demandeMatEtab.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DemandeMatEtab());
  }
}
