import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepartementComponent } from './list/departement.component';
import { DepartementDetailComponent } from './detail/departement-detail.component';
import { DepartementUpdateComponent } from './update/departement-update.component';
import { DepartementDeleteDialogComponent } from './delete/departement-delete-dialog.component';
import { DepartementRoutingModule } from './route/departement-routing.module';

@NgModule({
  imports: [SharedModule, DepartementRoutingModule],
  declarations: [DepartementComponent, DepartementDetailComponent, DepartementUpdateComponent, DepartementDeleteDialogComponent],
  entryComponents: [DepartementDeleteDialogComponent],
})
export class DepartementModule {}
