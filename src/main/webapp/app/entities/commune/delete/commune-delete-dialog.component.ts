import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommune } from '../commune.model';
import { CommuneService } from '../service/commune.service';

@Component({
  templateUrl: './commune-delete-dialog.component.html',
})
export class CommuneDeleteDialogComponent {
  commune?: ICommune;

  constructor(protected communeService: CommuneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.communeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
