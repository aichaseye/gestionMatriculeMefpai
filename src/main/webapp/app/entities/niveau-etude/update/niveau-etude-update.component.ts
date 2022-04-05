import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { INiveauEtude, NiveauEtude } from '../niveau-etude.model';
import { NiveauEtudeService } from '../service/niveau-etude.service';
import { Niveau } from 'app/entities/enumerations/niveau.model';

@Component({
  selector: 'jhi-niveau-etude-update',
  templateUrl: './niveau-etude-update.component.html',
})
export class NiveauEtudeUpdateComponent implements OnInit {
  isSaving = false;
  niveauValues = Object.keys(Niveau);

  editForm = this.fb.group({
    id: [],
    niveau: [null, [Validators.required]],
  });

  constructor(protected niveauEtudeService: NiveauEtudeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ niveauEtude }) => {
      this.updateForm(niveauEtude);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const niveauEtude = this.createFromForm();
    if (niveauEtude.id !== undefined) {
      this.subscribeToSaveResponse(this.niveauEtudeService.update(niveauEtude));
    } else {
      this.subscribeToSaveResponse(this.niveauEtudeService.create(niveauEtude));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INiveauEtude>>): void {
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

  protected updateForm(niveauEtude: INiveauEtude): void {
    this.editForm.patchValue({
      id: niveauEtude.id,
      niveau: niveauEtude.niveau,
    });
  }

  protected createFromForm(): INiveauEtude {
    return {
      ...new NiveauEtude(),
      id: this.editForm.get(['id'])!.value,
      niveau: this.editForm.get(['niveau'])!.value,
    };
  }
}
