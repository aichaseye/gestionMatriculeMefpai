import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDiplome } from '../diplome.model';
import { DiplomeService } from '../service/diplome.service';

@Component({
  templateUrl: './diplome-delete-dialog.component.html',
})
export class DiplomeDeleteDialogComponent {
  diplome?: IDiplome;

  constructor(protected diplomeService: DiplomeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.diplomeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
