import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDiplome, Diplome } from '../diplome.model';
import { DiplomeService } from '../service/diplome.service';

@Injectable({ providedIn: 'root' })
export class DiplomeRoutingResolveService implements Resolve<IDiplome> {
  constructor(protected service: DiplomeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDiplome> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((diplome: HttpResponse<Diplome>) => {
          if (diplome.body) {
            return of(diplome.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Diplome());
  }
}
