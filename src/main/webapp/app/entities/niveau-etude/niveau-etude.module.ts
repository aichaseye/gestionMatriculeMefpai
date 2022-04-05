import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NiveauEtudeComponent } from './list/niveau-etude.component';
import { NiveauEtudeDetailComponent } from './detail/niveau-etude-detail.component';
import { NiveauEtudeUpdateComponent } from './update/niveau-etude-update.component';
import { NiveauEtudeDeleteDialogComponent } from './delete/niveau-etude-delete-dialog.component';
import { NiveauEtudeRoutingModule } from './route/niveau-etude-routing.module';

@NgModule({
  imports: [SharedModule, NiveauEtudeRoutingModule],
  declarations: [NiveauEtudeComponent, NiveauEtudeDetailComponent, NiveauEtudeUpdateComponent, NiveauEtudeDeleteDialogComponent],
  entryComponents: [NiveauEtudeDeleteDialogComponent],
})
export class NiveauEtudeModule {}
