import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFiliereEtude } from '../filiere-etude.model';
import { FiliereEtudeService } from '../service/filiere-etude.service';

@Component({
  templateUrl: './filiere-etude-delete-dialog.component.html',
})
export class FiliereEtudeDeleteDialogComponent {
  filiereEtude?: IFiliereEtude;

  constructor(protected filiereEtudeService: FiliereEtudeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.filiereEtudeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
