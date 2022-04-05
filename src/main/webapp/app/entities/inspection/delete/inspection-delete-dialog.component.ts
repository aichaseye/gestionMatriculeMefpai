import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInspection } from '../inspection.model';
import { InspectionService } from '../service/inspection.service';

@Component({
  templateUrl: './inspection-delete-dialog.component.html',
})
export class InspectionDeleteDialogComponent {
  inspection?: IInspection;

  constructor(protected inspectionService: InspectionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inspectionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
