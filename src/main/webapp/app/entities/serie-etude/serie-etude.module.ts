import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SerieEtudeComponent } from './list/serie-etude.component';
import { SerieEtudeDetailComponent } from './detail/serie-etude-detail.component';
import { SerieEtudeUpdateComponent } from './update/serie-etude-update.component';
import { SerieEtudeDeleteDialogComponent } from './delete/serie-etude-delete-dialog.component';
import { SerieEtudeRoutingModule } from './route/serie-etude-routing.module';

@NgModule({
  imports: [SharedModule, SerieEtudeRoutingModule],
  declarations: [SerieEtudeComponent, SerieEtudeDetailComponent, SerieEtudeUpdateComponent, SerieEtudeDeleteDialogComponent],
  entryComponents: [SerieEtudeDeleteDialogComponent],
})
export class SerieEtudeModule {}
