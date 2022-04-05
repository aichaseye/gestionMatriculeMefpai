import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInscription } from '../inscription.model';
import { InscriptionService } from '../service/inscription.service';

@Component({
  templateUrl: './inscription-delete-dialog.component.html',
})
export class InscriptionDeleteDialogComponent {
  inscription?: IInscription;

  constructor(protected inscriptionService: InscriptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inscriptionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
