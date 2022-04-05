import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NiveauEtudeComponent } from '../list/niveau-etude.component';
import { NiveauEtudeDetailComponent } from '../detail/niveau-etude-detail.component';
import { NiveauEtudeUpdateComponent } from '../update/niveau-etude-update.component';
import { NiveauEtudeRoutingResolveService } from './niveau-etude-routing-resolve.service';

const niveauEtudeRoute: Routes = [
  {
    path: '',
    component: NiveauEtudeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NiveauEtudeDetailComponent,
    resolve: {
      niveauEtude: NiveauEtudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NiveauEtudeUpdateComponent,
    resolve: {
      niveauEtude: NiveauEtudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NiveauEtudeUpdateComponent,
    resolve: {
      niveauEtude: NiveauEtudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(niveauEtudeRoute)],
  exports: [RouterModule],
})
export class NiveauEtudeRoutingModule {}
