import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPoste } from '../poste.model';
import { PosteService } from '../service/poste.service';

@Component({
  templateUrl: './poste-delete-dialog.component.html',
})
export class PosteDeleteDialogComponent {
  poste?: IPoste;

  constructor(protected posteService: PosteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.posteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
