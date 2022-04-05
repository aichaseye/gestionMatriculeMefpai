import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeMatApp } from '../demande-mat-app.model';
import { DemandeMatAppService } from '../service/demande-mat-app.service';

@Component({
  templateUrl: './demande-mat-app-delete-dialog.component.html',
})
export class DemandeMatAppDeleteDialogComponent {
  demandeMatApp?: IDemandeMatApp;

  constructor(protected demandeMatAppService: DemandeMatAppService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeMatAppService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
