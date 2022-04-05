import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReleveNote, ReleveNote } from '../releve-note.model';
import { ReleveNoteService } from '../service/releve-note.service';

@Injectable({ providedIn: 'root' })
export class ReleveNoteRoutingResolveService implements Resolve<IReleveNote> {
  constructor(protected service: ReleveNoteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReleveNote> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((releveNote: HttpResponse<ReleveNote>) => {
          if (releveNote.body) {
            return of(releveNote.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReleveNote());
  }
}
