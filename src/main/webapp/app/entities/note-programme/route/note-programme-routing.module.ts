import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NoteProgrammeComponent } from '../list/note-programme.component';
import { NoteProgrammeDetailComponent } from '../detail/note-programme-detail.component';
import { NoteProgrammeUpdateComponent } from '../update/note-programme-update.component';
import { NoteProgrammeRoutingResolveService } from './note-programme-routing-resolve.service';

const noteProgrammeRoute: Routes = [
  {
    path: '',
    component: NoteProgrammeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NoteProgrammeDetailComponent,
    resolve: {
      noteProgramme: NoteProgrammeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NoteProgrammeUpdateComponent,
    resolve: {
      noteProgramme: NoteProgrammeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NoteProgrammeUpdateComponent,
    resolve: {
      noteProgramme: NoteProgrammeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(noteProgrammeRoute)],
  exports: [RouterModule],
})
export class NoteProgrammeRoutingModule {}
