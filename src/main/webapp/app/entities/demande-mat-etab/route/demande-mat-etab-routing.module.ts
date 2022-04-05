import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemandeMatEtabComponent } from '../list/demande-mat-etab.component';
import { DemandeMatEtabDetailComponent } from '../detail/demande-mat-etab-detail.component';
import { DemandeMatEtabUpdateComponent } from '../update/demande-mat-etab-update.component';
import { DemandeMatEtabRoutingResolveService } from './demande-mat-etab-routing-resolve.service';

const demandeMatEtabRoute: Routes = [
  {
    path: '',
    component: DemandeMatEtabComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeMatEtabDetailComponent,
    resolve: {
      demandeMatEtab: DemandeMatEtabRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeMatEtabUpdateComponent,
    resolve: {
      demandeMatEtab: DemandeMatEtabRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeMatEtabUpdateComponent,
    resolve: {
      demandeMatEtab: DemandeMatEtabRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demandeMatEtabRoute)],
  exports: [RouterModule],
})
export class DemandeMatEtabRoutingModule {}
