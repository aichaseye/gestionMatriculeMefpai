import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NoteProgrammeComponent } from './list/note-programme.component';
import { NoteProgrammeDetailComponent } from './detail/note-programme-detail.component';
import { NoteProgrammeUpdateComponent } from './update/note-programme-update.component';
import { NoteProgrammeDeleteDialogComponent } from './delete/note-programme-delete-dialog.component';
import { NoteProgrammeRoutingModule } from './route/note-programme-routing.module';

@NgModule({
  imports: [SharedModule, NoteProgrammeRoutingModule],
  declarations: [NoteProgrammeComponent, NoteProgrammeDetailComponent, NoteProgrammeUpdateComponent, NoteProgrammeDeleteDialogComponent],
  entryComponents: [NoteProgrammeDeleteDialogComponent],
})
export class NoteProgrammeModule {}
