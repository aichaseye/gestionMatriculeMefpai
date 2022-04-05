import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDemandeMatEtab, DemandeMatEtab } from '../demande-mat-etab.model';
import { DemandeMatEtabService } from '../service/demande-mat-etab.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

@Component({
  selector: 'jhi-demande-mat-etab-update',
  templateUrl: './demande-mat-etab-update.component.html',
})
export class DemandeMatEtabUpdateComponent implements OnInit {
  isSaving = false;

  etablissementsCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    objet: [],
    description: [],
    dateDemande: [],
    etablissement: [],
  });

  constructor(
    protected demandeMatEtabService: DemandeMatEtabService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeMatEtab }) => {
      this.updateForm(demandeMatEtab);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demandeMatEtab = this.createFromForm();
    if (demandeMatEtab.id !== undefined) {
      this.subscribeToSaveResponse(this.demandeMatEtabService.update(demandeMatEtab));
    } else {
      this.subscribeToSaveResponse(this.demandeMatEtabService.create(demandeMatEtab));
    }
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandeMatEtab>>): void {
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

  protected updateForm(demandeMatEtab: IDemandeMatEtab): void {
    this.editForm.patchValue({
      id: demandeMatEtab.id,
      objet: demandeMatEtab.objet,
      description: demandeMatEtab.description,
      dateDemande: demandeMatEtab.dateDemande,
      etablissement: demandeMatEtab.etablissement,
    });

    this.etablissementsCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsCollection,
      demandeMatEtab.etablissement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etablissementService
      .query({ filter: 'demandematetab-is-null' })
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(etablissements, this.editForm.get('etablissement')!.value)
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsCollection = etablissements));
  }

  protected createFromForm(): IDemandeMatEtab {
    return {
      ...new DemandeMatEtab(),
      id: this.editForm.get(['id'])!.value,
      objet: this.editForm.get(['objet'])!.value,
      description: this.editForm.get(['description'])!.value,
      dateDemande: this.editForm.get(['dateDemande'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
    };
  }
}
