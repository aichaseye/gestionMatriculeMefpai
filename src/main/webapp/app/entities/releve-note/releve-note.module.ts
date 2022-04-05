import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReleveNoteComponent } from './list/releve-note.component';
import { ReleveNoteDetailComponent } from './detail/releve-note-detail.component';
import { ReleveNoteUpdateComponent } from './update/releve-note-update.component';
import { ReleveNoteDeleteDialogComponent } from './delete/releve-note-delete-dialog.component';
import { ReleveNoteRoutingModule } from './route/releve-note-routing.module';

@NgModule({
  imports: [SharedModule, ReleveNoteRoutingModule],
  declarations: [ReleveNoteComponent, ReleveNoteDetailComponent, ReleveNoteUpdateComponent, ReleveNoteDeleteDialogComponent],
  entryComponents: [ReleveNoteDeleteDialogComponent],
})
export class ReleveNoteModule {}
