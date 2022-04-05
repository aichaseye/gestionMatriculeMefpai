import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatiereComponent } from '../list/matiere.component';
import { MatiereDetailComponent } from '../detail/matiere-detail.component';
import { MatiereUpdateComponent } from '../update/matiere-update.component';
import { MatiereRoutingResolveService } from './matiere-routing-resolve.service';

const matiereRoute: Routes = [
  {
    path: '',
    component: MatiereComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MatiereDetailComponent,
    resolve: {
      matiere: MatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MatiereUpdateComponent,
    resolve: {
      matiere: MatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MatiereUpdateComponent,
    resolve: {
      matiere: MatiereRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(matiereRoute)],
  exports: [RouterModule],
})
export class MatiereRoutingModule {}
