import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonnelComponent } from './list/personnel.component';
import { PersonnelDetailComponent } from './detail/personnel-detail.component';
import { PersonnelUpdateComponent } from './update/personnel-update.component';
import { PersonnelDeleteDialogComponent } from './delete/personnel-delete-dialog.component';
import { PersonnelRoutingModule } from './route/personnel-routing.module';

@NgModule({
  imports: [SharedModule, PersonnelRoutingModule],
  declarations: [PersonnelComponent, PersonnelDetailComponent, PersonnelUpdateComponent, PersonnelDeleteDialogComponent],
  entryComponents: [PersonnelDeleteDialogComponent],
})
export class PersonnelModule {}
