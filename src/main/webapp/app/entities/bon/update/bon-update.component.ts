import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBon, Bon } from '../bon.model';
import { BonService } from '../service/bon.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

@Component({
  selector: 'jhi-bon-update',
  templateUrl: './bon-update.component.html',
})
export class BonUpdateComponent implements OnInit {
  isSaving = false;

  etablissementsSharedCollection: IEtablissement[] = [];
  matieresSharedCollection: IMatiere[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    date: [],
    description: [],
    etablissement: [],
    matiere: [],
  });

  constructor(
    protected bonService: BonService,
    protected etablissementService: EtablissementService,
    protected matiereService: MatiereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bon }) => {
      this.updateForm(bon);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bon = this.createFromForm();
    if (bon.id !== undefined) {
      this.subscribeToSaveResponse(this.bonService.update(bon));
    } else {
      this.subscribeToSaveResponse(this.bonService.create(bon));
    }
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  trackMatiereById(index: number, item: IMatiere): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBon>>): void {
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

  protected updateForm(bon: IBon): void {
    this.editForm.patchValue({
      id: bon.id,
      quantite: bon.quantite,
      date: bon.date,
      description: bon.description,
      etablissement: bon.etablissement,
      matiere: bon.matiere,
    });

    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      bon.etablissement
    );
    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(this.matieresSharedCollection, bon.matiere);
  }

  protected loadRelationshipsOptions(): void {
    this.etablissementService
      .query()
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(etablissements, this.editForm.get('etablissement')!.value)
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsSharedCollection = etablissements));

    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));
  }

  protected createFromForm(): IBon {
    return {
      ...new Bon(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      date: this.editForm.get(['date'])!.value,
      description: this.editForm.get(['description'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
      matiere: this.editForm.get(['matiere'])!.value,
    };
  }
}
