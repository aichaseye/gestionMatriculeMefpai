import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReleveNoteComponent } from '../list/releve-note.component';
import { ReleveNoteDetailComponent } from '../detail/releve-note-detail.component';
import { ReleveNoteUpdateComponent } from '../update/releve-note-update.component';
import { ReleveNoteRoutingResolveService } from './releve-note-routing-resolve.service';

const releveNoteRoute: Routes = [
  {
    path: '',
    component: ReleveNoteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReleveNoteDetailComponent,
    resolve: {
      releveNote: ReleveNoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReleveNoteUpdateComponent,
    resolve: {
      releveNote: ReleveNoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReleveNoteUpdateComponent,
    resolve: {
      releveNote: ReleveNoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(releveNoteRoute)],
  exports: [RouterModule],
})
export class ReleveNoteRoutingModule {}
