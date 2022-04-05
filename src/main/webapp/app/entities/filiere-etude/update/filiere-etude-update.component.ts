import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFiliereEtude, FiliereEtude } from '../filiere-etude.model';
import { FiliereEtudeService } from '../service/filiere-etude.service';
import { Filiere } from 'app/entities/enumerations/filiere.model';

@Component({
  selector: 'jhi-filiere-etude-update',
  templateUrl: './filiere-etude-update.component.html',
})
export class FiliereEtudeUpdateComponent implements OnInit {
  isSaving = false;
  filiereValues = Object.keys(Filiere);

  editForm = this.fb.group({
    id: [],
    filiere: [null, [Validators.required]],
  });

  constructor(protected filiereEtudeService: FiliereEtudeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filiereEtude }) => {
      this.updateForm(filiereEtude);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filiereEtude = this.createFromForm();
    if (filiereEtude.id !== undefined) {
      this.subscribeToSaveResponse(this.filiereEtudeService.update(filiereEtude));
    } else {
      this.subscribeToSaveResponse(this.filiereEtudeService.create(filiereEtude));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiliereEtude>>): void {
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

  protected updateForm(filiereEtude: IFiliereEtude): void {
    this.editForm.patchValue({
      id: filiereEtude.id,
      filiere: filiereEtude.filiere,
    });
  }

  protected createFromForm(): IFiliereEtude {
    return {
      ...new FiliereEtude(),
      id: this.editForm.get(['id'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
    };
  }
}
