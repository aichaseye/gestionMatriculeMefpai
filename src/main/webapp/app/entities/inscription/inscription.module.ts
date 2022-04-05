import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InscriptionComponent } from './list/inscription.component';
import { InscriptionDetailComponent } from './detail/inscription-detail.component';
import { InscriptionUpdateComponent } from './update/inscription-update.component';
import { InscriptionDeleteDialogComponent } from './delete/inscription-delete-dialog.component';
import { InscriptionRoutingModule } from './route/inscription-routing.module';

@NgModule({
  imports: [SharedModule, InscriptionRoutingModule],
  declarations: [InscriptionComponent, InscriptionDetailComponent, InscriptionUpdateComponent, InscriptionDeleteDialogComponent],
  entryComponents: [InscriptionDeleteDialogComponent],
})
export class InscriptionModule {}
