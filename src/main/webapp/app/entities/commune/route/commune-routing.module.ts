import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommuneComponent } from '../list/commune.component';
import { CommuneDetailComponent } from '../detail/commune-detail.component';
import { CommuneUpdateComponent } from '../update/commune-update.component';
import { CommuneRoutingResolveService } from './commune-routing-resolve.service';

const communeRoute: Routes = [
  {
    path: '',
    component: CommuneComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommuneDetailComponent,
    resolve: {
      commune: CommuneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommuneUpdateComponent,
    resolve: {
      commune: CommuneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommuneUpdateComponent,
    resolve: {
      commune: CommuneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(communeRoute)],
  exports: [RouterModule],
})
export class CommuneRoutingModule {}
