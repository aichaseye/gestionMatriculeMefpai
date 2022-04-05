import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPays } from '../pays.model';
import { PaysService } from '../service/pays.service';

@Component({
  templateUrl: './pays-delete-dialog.component.html',
})
export class PaysDeleteDialogComponent {
  pays?: IPays;

  constructor(protected paysService: PaysService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paysService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
