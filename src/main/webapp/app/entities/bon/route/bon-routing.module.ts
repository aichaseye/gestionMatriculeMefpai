import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BonComponent } from '../list/bon.component';
import { BonDetailComponent } from '../detail/bon-detail.component';
import { BonUpdateComponent } from '../update/bon-update.component';
import { BonRoutingResolveService } from './bon-routing-resolve.service';

const bonRoute: Routes = [
  {
    path: '',
    component: BonComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BonDetailComponent,
    resolve: {
      bon: BonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BonUpdateComponent,
    resolve: {
      bon: BonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BonUpdateComponent,
    resolve: {
      bon: BonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bonRoute)],
  exports: [RouterModule],
})
export class BonRoutingModule {}
