import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InspectionComponent } from '../list/inspection.component';
import { InspectionDetailComponent } from '../detail/inspection-detail.component';
import { InspectionUpdateComponent } from '../update/inspection-update.component';
import { InspectionRoutingResolveService } from './inspection-routing-resolve.service';

const inspectionRoute: Routes = [
  {
    path: '',
    component: InspectionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InspectionDetailComponent,
    resolve: {
      inspection: InspectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InspectionUpdateComponent,
    resolve: {
      inspection: InspectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InspectionUpdateComponent,
    resolve: {
      inspection: InspectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inspectionRoute)],
  exports: [RouterModule],
})
export class InspectionRoutingModule {}
