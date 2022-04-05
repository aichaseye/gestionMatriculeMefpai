import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IImage, Image } from '../image.model';
import { ImageService } from '../service/image.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

@Component({
  selector: 'jhi-image-update',
  templateUrl: './image-update.component.html',
})
export class ImageUpdateComponent implements OnInit {
  isSaving = false;

  matieresSharedCollection: IMatiere[] = [];

  editForm = this.fb.group({
    id: [],
    url: [],
    matiere: [],
  });

  constructor(
    protected imageService: ImageService,
    protected matiereService: MatiereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ image }) => {
      this.updateForm(image);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const image = this.createFromForm();
    if (image.id !== undefined) {
      this.subscribeToSaveResponse(this.imageService.update(image));
    } else {
      this.subscribeToSaveResponse(this.imageService.create(image));
    }
  }

  trackMatiereById(index: number, item: IMatiere): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(image: IImage): void {
    this.editForm.patchValue({
      id: image.id,
      url: image.url,
      matiere: image.matiere,
    });

    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(this.matieresSharedCollection, image.matiere);
  }

  protected loadRelationshipsOptions(): void {
    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));
  }

  protected createFromForm(): IImage {
    return {
      ...new Image(),
      id: this.editForm.get(['id'])!.value,
      url: this.editForm.get(['url'])!.value,
      matiere: this.editForm.get(['matiere'])!.value,
    };
  }
}
