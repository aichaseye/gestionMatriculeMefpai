import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommuneComponent } from './list/commune.component';
import { CommuneDetailComponent } from './detail/commune-detail.component';
import { CommuneUpdateComponent } from './update/commune-update.component';
import { CommuneDeleteDialogComponent } from './delete/commune-delete-dialog.component';
import { CommuneRoutingModule } from './route/commune-routing.module';

@NgModule({
  imports: [SharedModule, CommuneRoutingModule],
  declarations: [CommuneComponent, CommuneDetailComponent, CommuneUpdateComponent, CommuneDeleteDialogComponent],
  entryComponents: [CommuneDeleteDialogComponent],
})
export class CommuneModule {}
