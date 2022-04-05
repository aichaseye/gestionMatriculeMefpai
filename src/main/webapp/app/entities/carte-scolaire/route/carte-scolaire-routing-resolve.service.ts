import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICarteScolaire, CarteScolaire } from '../carte-scolaire.model';
import { CarteScolaireService } from '../service/carte-scolaire.service';

@Injectable({ providedIn: 'root' })
export class CarteScolaireRoutingResolveService implements Resolve<ICarteScolaire> {
  constructor(protected service: CarteScolaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICarteScolaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((carteScolaire: HttpResponse<CarteScolaire>) => {
          if (carteScolaire.body) {
            return of(carteScolaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CarteScolaire());
  }
}
