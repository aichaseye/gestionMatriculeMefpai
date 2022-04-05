import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBon, Bon } from '../bon.model';
import { BonService } from '../service/bon.service';

@Injectable({ providedIn: 'root' })
export class BonRoutingResolveService implements Resolve<IBon> {
  constructor(protected service: BonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBon> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bon: HttpResponse<Bon>) => {
          if (bon.body) {
            return of(bon.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bon());
  }
}
