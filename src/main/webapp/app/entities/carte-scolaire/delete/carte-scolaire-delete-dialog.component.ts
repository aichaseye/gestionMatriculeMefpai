import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarteScolaire } from '../carte-scolaire.model';
import { CarteScolaireService } from '../service/carte-scolaire.service';

@Component({
  templateUrl: './carte-scolaire-delete-dialog.component.html',
})
export class CarteScolaireDeleteDialogComponent {
  carteScolaire?: ICarteScolaire;

  constructor(protected carteScolaireService: CarteScolaireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.carteScolaireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
