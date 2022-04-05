import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemandeMatAppComponent } from './list/demande-mat-app.component';
import { DemandeMatAppDetailComponent } from './detail/demande-mat-app-detail.component';
import { DemandeMatAppUpdateComponent } from './update/demande-mat-app-update.component';
import { DemandeMatAppDeleteDialogComponent } from './delete/demande-mat-app-delete-dialog.component';
import { DemandeMatAppRoutingModule } from './route/demande-mat-app-routing.module';

@NgModule({
  imports: [SharedModule, DemandeMatAppRoutingModule],
  declarations: [DemandeMatAppComponent, DemandeMatAppDetailComponent, DemandeMatAppUpdateComponent, DemandeMatAppDeleteDialogComponent],
  entryComponents: [DemandeMatAppDeleteDialogComponent],
})
export class DemandeMatAppModule {}
