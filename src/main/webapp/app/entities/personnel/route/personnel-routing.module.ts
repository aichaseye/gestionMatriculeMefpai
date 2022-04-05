import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonnelComponent } from '../list/personnel.component';
import { PersonnelDetailComponent } from '../detail/personnel-detail.component';
import { PersonnelUpdateComponent } from '../update/personnel-update.component';
import { PersonnelRoutingResolveService } from './personnel-routing-resolve.service';

const personnelRoute: Routes = [
  {
    path: '',
    component: PersonnelComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonnelDetailComponent,
    resolve: {
      personnel: PersonnelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonnelUpdateComponent,
    resolve: {
      personnel: PersonnelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonnelUpdateComponent,
    resolve: {
      personnel: PersonnelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personnelRoute)],
  exports: [RouterModule],
})
export class PersonnelRoutingModule {}
