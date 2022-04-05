import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApprenantComponent } from './list/apprenant.component';
import { ApprenantDetailComponent } from './detail/apprenant-detail.component';
import { ApprenantUpdateComponent } from './update/apprenant-update.component';
import { ApprenantDeleteDialogComponent } from './delete/apprenant-delete-dialog.component';
import { ApprenantRoutingModule } from './route/apprenant-routing.module';

@NgModule({
  imports: [SharedModule, ApprenantRoutingModule],
  declarations: [ApprenantComponent, ApprenantDetailComponent, ApprenantUpdateComponent, ApprenantDeleteDialogComponent],
  entryComponents: [ApprenantDeleteDialogComponent],
})
export class ApprenantModule {}
