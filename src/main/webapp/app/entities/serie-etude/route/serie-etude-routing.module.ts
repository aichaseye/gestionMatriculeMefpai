import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SerieEtudeComponent } from '../list/serie-etude.component';
import { SerieEtudeDetailComponent } from '../detail/serie-etude-detail.component';
import { SerieEtudeUpdateComponent } from '../update/serie-etude-update.component';
import { SerieEtudeRoutingResolveService } from './serie-etude-routing-resolve.service';

const serieEtudeRoute: Routes = [
  {
    path: '',
    component: SerieEtudeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SerieEtudeDetailComponent,
    resolve: {
      serieEtude: SerieEtudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SerieEtudeUpdateComponent,
    resolve: {
      serieEtude: SerieEtudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SerieEtudeUpdateComponent,
    resolve: {
      serieEtude: SerieEtudeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serieEtudeRoute)],
  exports: [RouterModule],
})
export class SerieEtudeRoutingModule {}
