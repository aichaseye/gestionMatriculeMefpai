import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IImage } from '../image.model';
import { ImageService } from '../service/image.service';

@Component({
  templateUrl: './image-delete-dialog.component.html',
})
export class ImageDeleteDialogComponent {
  image?: IImage;

  constructor(protected imageService: ImageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
