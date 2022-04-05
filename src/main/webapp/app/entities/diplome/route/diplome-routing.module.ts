import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DiplomeComponent } from '../list/diplome.component';
import { DiplomeDetailComponent } from '../detail/diplome-detail.component';
import { DiplomeUpdateComponent } from '../update/diplome-update.component';
import { DiplomeRoutingResolveService } from './diplome-routing-resolve.service';

const diplomeRoute: Routes = [
  {
    path: '',
    component: DiplomeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DiplomeDetailComponent,
    resolve: {
      diplome: DiplomeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DiplomeUpdateComponent,
    resolve: {
      diplome: DiplomeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DiplomeUpdateComponent,
    resolve: {
      diplome: DiplomeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(diplomeRoute)],
  exports: [RouterModule],
})
export class DiplomeRoutingModule {}
