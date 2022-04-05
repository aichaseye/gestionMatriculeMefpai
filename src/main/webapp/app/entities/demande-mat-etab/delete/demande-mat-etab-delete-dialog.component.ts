import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeMatEtab } from '../demande-mat-etab.model';
import { DemandeMatEtabService } from '../service/demande-mat-etab.service';

@Component({
  templateUrl: './demande-mat-etab-delete-dialog.component.html',
})
export class DemandeMatEtabDeleteDialogComponent {
  demandeMatEtab?: IDemandeMatEtab;

  constructor(protected demandeMatEtabService: DemandeMatEtabService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeMatEtabService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
