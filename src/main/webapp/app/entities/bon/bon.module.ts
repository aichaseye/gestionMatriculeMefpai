import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BonComponent } from './list/bon.component';
import { BonDetailComponent } from './detail/bon-detail.component';
import { BonUpdateComponent } from './update/bon-update.component';
import { BonDeleteDialogComponent } from './delete/bon-delete-dialog.component';
import { BonRoutingModule } from './route/bon-routing.module';

@NgModule({
  imports: [SharedModule, BonRoutingModule],
  declarations: [BonComponent, BonDetailComponent, BonUpdateComponent, BonDeleteDialogComponent],
  entryComponents: [BonDeleteDialogComponent],
})
export class BonModule {}
