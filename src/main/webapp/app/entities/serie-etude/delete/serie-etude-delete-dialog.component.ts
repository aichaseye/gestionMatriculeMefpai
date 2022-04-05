import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISerieEtude } from '../serie-etude.model';
import { SerieEtudeService } from '../service/serie-etude.service';

@Component({
  templateUrl: './serie-etude-delete-dialog.component.html',
})
export class SerieEtudeDeleteDialogComponent {
  serieEtude?: ISerieEtude;

  constructor(protected serieEtudeService: SerieEtudeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serieEtudeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
