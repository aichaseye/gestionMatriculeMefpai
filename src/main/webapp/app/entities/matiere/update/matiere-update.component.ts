import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMatiere, Matiere } from '../matiere.model';
import { MatiereService } from '../service/matiere.service';

@Component({
  selector: 'jhi-matiere-update',
  templateUrl: './matiere-update.component.html',
})
export class MatiereUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomMatiere: [],
    reference: [],
    typeMatiere: [],
    matriculeMatiere: [],
  });

  constructor(protected matiereService: MatiereService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matiere }) => {
      this.updateForm(matiere);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matiere = this.createFromForm();
    if (matiere.id !== undefined) {
      this.subscribeToSaveResponse(this.matiereService.update(matiere));
    } else {
      this.subscribeToSaveResponse(this.matiereService.create(matiere));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatiere>>): void {
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

  protected updateForm(matiere: IMatiere): void {
    this.editForm.patchValue({
      id: matiere.id,
      nomMatiere: matiere.nomMatiere,
      reference: matiere.reference,
      typeMatiere: matiere.typeMatiere,
      matriculeMatiere: matiere.matriculeMatiere,
    });
  }

  protected createFromForm(): IMatiere {
    return {
      ...new Matiere(),
      id: this.editForm.get(['id'])!.value,
      nomMatiere: this.editForm.get(['nomMatiere'])!.value,
      reference: this.editForm.get(['reference'])!.value,
      typeMatiere: this.editForm.get(['typeMatiere'])!.value,
      matriculeMatiere: this.editForm.get(['matriculeMatiere'])!.value,
    };
  }
}
