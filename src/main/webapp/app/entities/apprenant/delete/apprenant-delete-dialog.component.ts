import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprenant } from '../apprenant.model';
import { ApprenantService } from '../service/apprenant.service';

@Component({
  templateUrl: './apprenant-delete-dialog.component.html',
})
export class ApprenantDeleteDialogComponent {
  apprenant?: IApprenant;

  constructor(protected apprenantService: ApprenantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apprenantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
