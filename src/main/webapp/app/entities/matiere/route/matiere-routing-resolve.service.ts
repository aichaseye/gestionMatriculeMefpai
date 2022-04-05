import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMatiere, Matiere } from '../matiere.model';
import { MatiereService } from '../service/matiere.service';

@Injectable({ providedIn: 'root' })
export class MatiereRoutingResolveService implements Resolve<IMatiere> {
  constructor(protected service: MatiereService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMatiere> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((matiere: HttpResponse<Matiere>) => {
          if (matiere.body) {
            return of(matiere.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Matiere());
  }
}
