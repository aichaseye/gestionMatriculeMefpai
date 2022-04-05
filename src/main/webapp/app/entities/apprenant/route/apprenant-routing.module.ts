import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApprenantComponent } from '../list/apprenant.component';
import { ApprenantDetailComponent } from '../detail/apprenant-detail.component';
import { ApprenantUpdateComponent } from '../update/apprenant-update.component';
import { ApprenantRoutingResolveService } from './apprenant-routing-resolve.service';

const apprenantRoute: Routes = [
  {
    path: '',
    component: ApprenantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApprenantDetailComponent,
    resolve: {
      apprenant: ApprenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApprenantUpdateComponent,
    resolve: {
      apprenant: ApprenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApprenantUpdateComponent,
    resolve: {
      apprenant: ApprenantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(apprenantRoute)],
  exports: [RouterModule],
})
export class ApprenantRoutingModule {}
