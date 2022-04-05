import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PaysComponent } from '../list/pays.component';
import { PaysDetailComponent } from '../detail/pays-detail.component';
import { PaysUpdateComponent } from '../update/pays-update.component';
import { PaysRoutingResolveService } from './pays-routing-resolve.service';

const paysRoute: Routes = [
  {
    path: '',
    component: PaysComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaysDetailComponent,
    resolve: {
      pays: PaysRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaysUpdateComponent,
    resolve: {
      pays: PaysRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaysUpdateComponent,
    resolve: {
      pays: PaysRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paysRoute)],
  exports: [RouterModule],
})
export class PaysRoutingModule {}
