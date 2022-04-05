import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFiliereEtude, FiliereEtude } from '../filiere-etude.model';
import { FiliereEtudeService } from '../service/filiere-etude.service';

@Injectable({ providedIn: 'root' })
export class FiliereEtudeRoutingResolveService implements Resolve<IFiliereEtude> {
  constructor(protected service: FiliereEtudeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFiliereEtude> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((filiereEtude: HttpResponse<FiliereEtude>) => {
          if (filiereEtude.body) {
            return of(filiereEtude.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FiliereEtude());
  }
}
