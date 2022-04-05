import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InspectionComponent } from './list/inspection.component';
import { InspectionDetailComponent } from './detail/inspection-detail.component';
import { InspectionUpdateComponent } from './update/inspection-update.component';
import { InspectionDeleteDialogComponent } from './delete/inspection-delete-dialog.component';
import { InspectionRoutingModule } from './route/inspection-routing.module';

@NgModule({
  imports: [SharedModule, InspectionRoutingModule],
  declarations: [InspectionComponent, InspectionDetailComponent, InspectionUpdateComponent, InspectionDeleteDialogComponent],
  entryComponents: [InspectionDeleteDialogComponent],
})
export class InspectionModule {}
