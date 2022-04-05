import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FiliereEtudeComponent } from '../list/filiere-etude.component';
import { FiliereEtudeDetailComponent } from '../detail/filiere-etude-detail.component';
import { FiliereEtudeUpdateComponent } from '../update/filiere-etude-update.component';
import { FiliereEtudeRoutingResolveService } from './filiere-etude-routing-resolve.service';

const filiereEtudeRoute: Routes = [
  {
    path: '',
    component: FiliereEtudeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FiliereEtudeDetailComponent,
    resolve: {
      filiereEtude: FiliereEtudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FiliereEtudeUpdateComponent,
    resolve: {
      filiereEtude: FiliereEtudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FiliereEtudeUpdateComponent,
    resolve: {
      filiereEtude: FiliereEtudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(filiereEtudeRoute)],
  exports: [RouterModule],
})
export class FiliereEtudeRoutingModule {}
