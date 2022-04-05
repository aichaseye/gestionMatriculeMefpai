import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDemandeMatApp, DemandeMatApp } from '../demande-mat-app.model';
import { DemandeMatAppService } from '../service/demande-mat-app.service';

@Component({
  selector: 'jhi-demande-mat-app-update',
  templateUrl: './demande-mat-app-update.component.html',
})
export class DemandeMatAppUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    objet: [],
    description: [],
    dateDemande: [],
  });

  constructor(protected demandeMatAppService: DemandeMatAppService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeMatApp }) => {
      this.updateForm(demandeMatApp);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demandeMatApp = this.createFromForm();
    if (demandeMatApp.id !== undefined) {
      this.subscribeToSaveResponse(this.demandeMatAppService.update(demandeMatApp));
    } else {
      this.subscribeToSaveResponse(this.demandeMatAppService.create(demandeMatApp));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandeMatApp>>): void {
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

  protected updateForm(demandeMatApp: IDemandeMatApp): void {
    this.editForm.patchValue({
      id: demandeMatApp.id,
      objet: demandeMatApp.objet,
      description: demandeMatApp.description,
      dateDemande: demandeMatApp.dateDemande,
    });
  }

  protected createFromForm(): IDemandeMatApp {
    return {
      ...new DemandeMatApp(),
      id: this.editForm.get(['id'])!.value,
      objet: this.editForm.get(['objet'])!.value,
      description: this.editForm.get(['description'])!.value,
      dateDemande: this.editForm.get(['dateDemande'])!.value,
    };
  }
}
