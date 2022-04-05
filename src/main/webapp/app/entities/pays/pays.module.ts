import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PaysComponent } from './list/pays.component';
import { PaysDetailComponent } from './detail/pays-detail.component';
import { PaysUpdateComponent } from './update/pays-update.component';
import { PaysDeleteDialogComponent } from './delete/pays-delete-dialog.component';
import { PaysRoutingModule } from './route/pays-routing.module';

@NgModule({
  imports: [SharedModule, PaysRoutingModule],
  declarations: [PaysComponent, PaysDetailComponent, PaysUpdateComponent, PaysDeleteDialogComponent],
  entryComponents: [PaysDeleteDialogComponent],
})
export class PaysModule {}
