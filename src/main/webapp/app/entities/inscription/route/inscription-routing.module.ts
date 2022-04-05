import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InscriptionComponent } from '../list/inscription.component';
import { InscriptionDetailComponent } from '../detail/inscription-detail.component';
import { InscriptionUpdateComponent } from '../update/inscription-update.component';
import { InscriptionRoutingResolveService } from './inscription-routing-resolve.service';

const inscriptionRoute: Routes = [
  {
    path: '',
    component: InscriptionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InscriptionDetailComponent,
    resolve: {
      inscription: InscriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InscriptionUpdateComponent,
    resolve: {
      inscription: InscriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InscriptionUpdateComponent,
    resolve: {
      inscription: InscriptionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inscriptionRoute)],
  exports: [RouterModule],
})
export class InscriptionRoutingModule {}
