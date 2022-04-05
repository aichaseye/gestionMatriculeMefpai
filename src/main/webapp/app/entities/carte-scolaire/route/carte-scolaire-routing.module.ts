import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CarteScolaireComponent } from '../list/carte-scolaire.component';
import { CarteScolaireDetailComponent } from '../detail/carte-scolaire-detail.component';
import { CarteScolaireUpdateComponent } from '../update/carte-scolaire-update.component';
import { CarteScolaireRoutingResolveService } from './carte-scolaire-routing-resolve.service';

const carteScolaireRoute: Routes = [
  {
    path: '',
    component: CarteScolaireComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CarteScolaireDetailComponent,
    resolve: {
      carteScolaire: CarteScolaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CarteScolaireUpdateComponent,
    resolve: {
      carteScolaire: CarteScolaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CarteScolaireUpdateComponent,
    resolve: {
      carteScolaire: CarteScolaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(carteScolaireRoute)],
  exports: [RouterModule],
})
export class CarteScolaireRoutingModule {}
