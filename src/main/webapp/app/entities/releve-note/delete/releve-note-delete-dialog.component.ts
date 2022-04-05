import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReleveNote } from '../releve-note.model';
import { ReleveNoteService } from '../service/releve-note.service';

@Component({
  templateUrl: './releve-note-delete-dialog.component.html',
})
export class ReleveNoteDeleteDialogComponent {
  releveNote?: IReleveNote;

  constructor(protected releveNoteService: ReleveNoteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.releveNoteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
