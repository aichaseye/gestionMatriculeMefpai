import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemandeMatEtabComponent } from './list/demande-mat-etab.component';
import { DemandeMatEtabDetailComponent } from './detail/demande-mat-etab-detail.component';
import { DemandeMatEtabUpdateComponent } from './update/demande-mat-etab-update.component';
import { DemandeMatEtabDeleteDialogComponent } from './delete/demande-mat-etab-delete-dialog.component';
import { DemandeMatEtabRoutingModule } from './route/demande-mat-etab-routing.module';

@NgModule({
  imports: [SharedModule, DemandeMatEtabRoutingModule],
  declarations: [
    DemandeMatEtabComponent,
    DemandeMatEtabDetailComponent,
    DemandeMatEtabUpdateComponent,
    DemandeMatEtabDeleteDialogComponent,
  ],
  entryComponents: [DemandeMatEtabDeleteDialogComponent],
})
export class DemandeMatEtabModule {}
