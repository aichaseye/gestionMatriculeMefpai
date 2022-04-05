import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INoteProgramme } from '../note-programme.model';
import { NoteProgrammeService } from '../service/note-programme.service';

@Component({
  templateUrl: './note-programme-delete-dialog.component.html',
})
export class NoteProgrammeDeleteDialogComponent {
  noteProgramme?: INoteProgramme;

  constructor(protected noteProgrammeService: NoteProgrammeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.noteProgrammeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
