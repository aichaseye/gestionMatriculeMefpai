import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepartement } from '../departement.model';
import { DepartementService } from '../service/departement.service';

@Component({
  templateUrl: './departement-delete-dialog.component.html',
})
export class DepartementDeleteDialogComponent {
  departement?: IDepartement;

  constructor(protected departementService: DepartementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.departementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
