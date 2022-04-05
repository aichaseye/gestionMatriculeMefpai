import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemandeMatAppComponent } from '../list/demande-mat-app.component';
import { DemandeMatAppDetailComponent } from '../detail/demande-mat-app-detail.component';
import { DemandeMatAppUpdateComponent } from '../update/demande-mat-app-update.component';
import { DemandeMatAppRoutingResolveService } from './demande-mat-app-routing-resolve.service';

const demandeMatAppRoute: Routes = [
  {
    path: '',
    component: DemandeMatAppComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeMatAppDetailComponent,
    resolve: {
      demandeMatApp: DemandeMatAppRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeMatAppUpdateComponent,
    resolve: {
      demandeMatApp: DemandeMatAppRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeMatAppUpdateComponent,
    resolve: {
      demandeMatApp: DemandeMatAppRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demandeMatAppRoute)],
  exports: [RouterModule],
})
export class DemandeMatAppRoutingModule {}
