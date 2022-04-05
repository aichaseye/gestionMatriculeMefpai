import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommune, Commune } from '../commune.model';
import { CommuneService } from '../service/commune.service';

@Injectable({ providedIn: 'root' })
export class CommuneRoutingResolveService implements Resolve<ICommune> {
  constructor(protected service: CommuneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommune> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((commune: HttpResponse<Commune>) => {
          if (commune.body) {
            return of(commune.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Commune());
  }
}
