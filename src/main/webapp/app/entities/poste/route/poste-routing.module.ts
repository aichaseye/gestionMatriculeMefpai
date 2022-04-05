import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PosteComponent } from '../list/poste.component';
import { PosteDetailComponent } from '../detail/poste-detail.component';
import { PosteUpdateComponent } from '../update/poste-update.component';
import { PosteRoutingResolveService } from './poste-routing-resolve.service';

const posteRoute: Routes = [
  {
    path: '',
    component: PosteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PosteDetailComponent,
    resolve: {
      poste: PosteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PosteUpdateComponent,
    resolve: {
      poste: PosteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PosteUpdateComponent,
    resolve: {
      poste: PosteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(posteRoute)],
  exports: [RouterModule],
})
export class PosteRoutingModule {}
