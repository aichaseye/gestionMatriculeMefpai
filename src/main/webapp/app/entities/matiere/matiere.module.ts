import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MatiereComponent } from './list/matiere.component';
import { MatiereDetailComponent } from './detail/matiere-detail.component';
import { MatiereUpdateComponent } from './update/matiere-update.component';
import { MatiereDeleteDialogComponent } from './delete/matiere-delete-dialog.component';
import { MatiereRoutingModule } from './route/matiere-routing.module';

@NgModule({
  imports: [SharedModule, MatiereRoutingModule],
  declarations: [MatiereComponent, MatiereDetailComponent, MatiereUpdateComponent, MatiereDeleteDialogComponent],
  entryComponents: [MatiereDeleteDialogComponent],
})
export class MatiereModule {}
