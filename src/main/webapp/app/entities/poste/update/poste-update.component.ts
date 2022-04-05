import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPoste, Poste } from '../poste.model';
import { PosteService } from '../service/poste.service';
import { NomPoste } from 'app/entities/enumerations/nom-poste.model';

@Component({
  selector: 'jhi-poste-update',
  templateUrl: './poste-update.component.html',
})
export class PosteUpdateComponent implements OnInit {
  isSaving = false;
  nomPosteValues = Object.keys(NomPoste);

  editForm = this.fb.group({
    id: [],
    nomPoste: [null, [Validators.required]],
  });

  constructor(protected posteService: PosteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ poste }) => {
      this.updateForm(poste);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const poste = this.createFromForm();
    if (poste.id !== undefined) {
      this.subscribeToSaveResponse(this.posteService.update(poste));
    } else {
      this.subscribeToSaveResponse(this.posteService.create(poste));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPoste>>): void {
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

  protected updateForm(poste: IPoste): void {
    this.editForm.patchValue({
      id: poste.id,
      nomPoste: poste.nomPoste,
    });
  }

  protected createFromForm(): IPoste {
    return {
      ...new Poste(),
      id: this.editForm.get(['id'])!.value,
      nomPoste: this.editForm.get(['nomPoste'])!.value,
    };
  }
}
