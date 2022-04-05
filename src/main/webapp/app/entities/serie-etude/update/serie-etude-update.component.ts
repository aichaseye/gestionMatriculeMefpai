import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISerieEtude, SerieEtude } from '../serie-etude.model';
import { SerieEtudeService } from '../service/serie-etude.service';
import { Serie } from 'app/entities/enumerations/serie.model';

@Component({
  selector: 'jhi-serie-etude-update',
  templateUrl: './serie-etude-update.component.html',
})
export class SerieEtudeUpdateComponent implements OnInit {
  isSaving = false;
  serieValues = Object.keys(Serie);

  editForm = this.fb.group({
    id: [],
    serie: [null, [Validators.required]],
  });

  constructor(protected serieEtudeService: SerieEtudeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serieEtude }) => {
      this.updateForm(serieEtude);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serieEtude = this.createFromForm();
    if (serieEtude.id !== undefined) {
      this.subscribeToSaveResponse(this.serieEtudeService.update(serieEtude));
    } else {
      this.subscribeToSaveResponse(this.serieEtudeService.create(serieEtude));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISerieEtude>>): void {
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

  protected updateForm(serieEtude: ISerieEtude): void {
    this.editForm.patchValue({
      id: serieEtude.id,
      serie: serieEtude.serie,
    });
  }

  protected createFromForm(): ISerieEtude {
    return {
      ...new SerieEtude(),
      id: this.editForm.get(['id'])!.value,
      serie: this.editForm.get(['serie'])!.value,
    };
  }
}
