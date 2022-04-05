import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INiveauEtude } from '../niveau-etude.model';
import { NiveauEtudeService } from '../service/niveau-etude.service';

@Component({
  templateUrl: './niveau-etude-delete-dialog.component.html',
})
export class NiveauEtudeDeleteDialogComponent {
  niveauEtude?: INiveauEtude;

  constructor(protected niveauEtudeService: NiveauEtudeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.niveauEtudeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
