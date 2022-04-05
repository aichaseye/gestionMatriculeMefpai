import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClasse } from '../classe.model';
import { ClasseService } from '../service/classe.service';

@Component({
  templateUrl: './classe-delete-dialog.component.html',
})
export class ClasseDeleteDialogComponent {
  classe?: IClasse;

  constructor(protected classeService: ClasseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
