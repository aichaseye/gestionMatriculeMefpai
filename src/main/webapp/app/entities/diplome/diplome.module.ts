import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DiplomeComponent } from './list/diplome.component';
import { DiplomeDetailComponent } from './detail/diplome-detail.component';
import { DiplomeUpdateComponent } from './update/diplome-update.component';
import { DiplomeDeleteDialogComponent } from './delete/diplome-delete-dialog.component';
import { DiplomeRoutingModule } from './route/diplome-routing.module';

@NgModule({
  imports: [SharedModule, DiplomeRoutingModule],
  declarations: [DiplomeComponent, DiplomeDetailComponent, DiplomeUpdateComponent, DiplomeDeleteDialogComponent],
  entryComponents: [DiplomeDeleteDialogComponent],
})
export class DiplomeModule {}
