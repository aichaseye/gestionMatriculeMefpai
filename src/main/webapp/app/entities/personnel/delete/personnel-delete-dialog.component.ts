import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonnel } from '../personnel.model';
import { PersonnelService } from '../service/personnel.service';

@Component({
  templateUrl: './personnel-delete-dialog.component.html',
})
export class PersonnelDeleteDialogComponent {
  personnel?: IPersonnel;

  constructor(protected personnelService: PersonnelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personnelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
