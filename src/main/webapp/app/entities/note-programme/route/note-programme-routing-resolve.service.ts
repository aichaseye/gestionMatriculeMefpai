import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INoteProgramme, NoteProgramme } from '../note-programme.model';
import { NoteProgrammeService } from '../service/note-programme.service';

@Injectable({ providedIn: 'root' })
export class NoteProgrammeRoutingResolveService implements Resolve<INoteProgramme> {
  constructor(protected service: NoteProgrammeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INoteProgramme> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((noteProgramme: HttpResponse<NoteProgramme>) => {
          if (noteProgramme.body) {
            return of(noteProgramme.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NoteProgramme());
  }
}
