import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FiliereEtudeComponent } from './list/filiere-etude.component';
import { FiliereEtudeDetailComponent } from './detail/filiere-etude-detail.component';
import { FiliereEtudeUpdateComponent } from './update/filiere-etude-update.component';
import { FiliereEtudeDeleteDialogComponent } from './delete/filiere-etude-delete-dialog.component';
import { FiliereEtudeRoutingModule } from './route/filiere-etude-routing.module';

@NgModule({
  imports: [SharedModule, FiliereEtudeRoutingModule],
  declarations: [FiliereEtudeComponent, FiliereEtudeDetailComponent, FiliereEtudeUpdateComponent, FiliereEtudeDeleteDialogComponent],
  entryComponents: [FiliereEtudeDeleteDialogComponent],
})
export class FiliereEtudeModule {}
