import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBon } from '../bon.model';
import { BonService } from '../service/bon.service';

@Component({
  templateUrl: './bon-delete-dialog.component.html',
})
export class BonDeleteDialogComponent {
  bon?: IBon;

  constructor(protected bonService: BonService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
