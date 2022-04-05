import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EtablissementComponent } from './list/etablissement.component';
import { EtablissementDetailComponent } from './detail/etablissement-detail.component';
import { EtablissementUpdateComponent } from './update/etablissement-update.component';
import { EtablissementDeleteDialogComponent } from './delete/etablissement-delete-dialog.component';
import { EtablissementRoutingModule } from './route/etablissement-routing.module';

@NgModule({
  imports: [SharedModule, EtablissementRoutingModule],
  declarations: [EtablissementComponent, EtablissementDetailComponent, EtablissementUpdateComponent, EtablissementDeleteDialogComponent],
  entryComponents: [EtablissementDeleteDialogComponent],
})
export class EtablissementModule {}
