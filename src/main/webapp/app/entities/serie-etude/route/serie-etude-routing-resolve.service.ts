import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISerieEtude, SerieEtude } from '../serie-etude.model';
import { SerieEtudeService } from '../service/serie-etude.service';

@Injectable({ providedIn: 'root' })
export class SerieEtudeRoutingResolveService implements Resolve<ISerieEtude> {
  constructor(protected service: SerieEtudeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISerieEtude> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((serieEtude: HttpResponse<SerieEtude>) => {
          if (serieEtude.body) {
            return of(serieEtude.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SerieEtude());
  }
}
