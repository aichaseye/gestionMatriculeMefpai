import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INiveauEtude, NiveauEtude } from '../niveau-etude.model';
import { NiveauEtudeService } from '../service/niveau-etude.service';

@Injectable({ providedIn: 'root' })
export class NiveauEtudeRoutingResolveService implements Resolve<INiveauEtude> {
  constructor(protected service: NiveauEtudeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INiveauEtude> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((niveauEtude: HttpResponse<NiveauEtude>) => {
          if (niveauEtude.body) {
            return of(niveauEtude.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NiveauEtude());
  }
}
