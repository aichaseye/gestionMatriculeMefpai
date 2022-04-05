import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CarteScolaireComponent } from './list/carte-scolaire.component';
import { CarteScolaireDetailComponent } from './detail/carte-scolaire-detail.component';
import { CarteScolaireUpdateComponent } from './update/carte-scolaire-update.component';
import { CarteScolaireDeleteDialogComponent } from './delete/carte-scolaire-delete-dialog.component';
import { CarteScolaireRoutingModule } from './route/carte-scolaire-routing.module';

@NgModule({
  imports: [SharedModule, CarteScolaireRoutingModule],
  declarations: [CarteScolaireComponent, CarteScolaireDetailComponent, CarteScolaireUpdateComponent, CarteScolaireDeleteDialogComponent],
  entryComponents: [CarteScolaireDeleteDialogComponent],
})
export class CarteScolaireModule {}
