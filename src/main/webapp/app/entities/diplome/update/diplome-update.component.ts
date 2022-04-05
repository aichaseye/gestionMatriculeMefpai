import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDiplome, Diplome } from '../diplome.model';
import { DiplomeService } from '../service/diplome.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IFiliereEtude } from 'app/entities/filiere-etude/filiere-etude.model';
import { FiliereEtudeService } from 'app/entities/filiere-etude/service/filiere-etude.service';
import { ISerieEtude } from 'app/entities/serie-etude/serie-etude.model';
import { SerieEtudeService } from 'app/entities/serie-etude/service/serie-etude.service';
import { INiveauEtude } from 'app/entities/niveau-etude/niveau-etude.model';
import { NiveauEtudeService } from 'app/entities/niveau-etude/service/niveau-etude.service';
import { NomDiplome } from 'app/entities/enumerations/nom-diplome.model';

@Component({
  selector: 'jhi-diplome-update',
  templateUrl: './diplome-update.component.html',
})
export class DiplomeUpdateComponent implements OnInit {
  isSaving = false;
  nomDiplomeValues = Object.keys(NomDiplome);

  apprenantsSharedCollection: IApprenant[] = [];
  filiereEtudesSharedCollection: IFiliereEtude[] = [];
  serieEtudesSharedCollection: ISerieEtude[] = [];
  niveauEtudesSharedCollection: INiveauEtude[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    matriculeDiplome: [null, [Validators.required]],
    annee: [],
    apprenant: [],
    filiereEtude: [],
    serieEtude: [],
    niveauEtude: [],
  });

  constructor(
    protected diplomeService: DiplomeService,
    protected apprenantService: ApprenantService,
    protected filiereEtudeService: FiliereEtudeService,
    protected serieEtudeService: SerieEtudeService,
    protected niveauEtudeService: NiveauEtudeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ diplome }) => {
      this.updateForm(diplome);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const diplome = this.createFromForm();
    if (diplome.id !== undefined) {
      this.subscribeToSaveResponse(this.diplomeService.update(diplome));
    } else {
      this.subscribeToSaveResponse(this.diplomeService.create(diplome));
    }
  }

  trackApprenantById(index: number, item: IApprenant): number {
    return item.id!;
  }

  trackFiliereEtudeById(index: number, item: IFiliereEtude): number {
    return item.id!;
  }

  trackSerieEtudeById(index: number, item: ISerieEtude): number {
    return item.id!;
  }

  trackNiveauEtudeById(index: number, item: INiveauEtude): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiplome>>): void {
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

  protected updateForm(diplome: IDiplome): void {
    this.editForm.patchValue({
      id: diplome.id,
      nom: diplome.nom,
      matriculeDiplome: diplome.matriculeDiplome,
      annee: diplome.annee,
      apprenant: diplome.apprenant,
      filiereEtude: diplome.filiereEtude,
      serieEtude: diplome.serieEtude,
      niveauEtude: diplome.niveauEtude,
    });

    this.apprenantsSharedCollection = this.apprenantService.addApprenantToCollectionIfMissing(
      this.apprenantsSharedCollection,
      diplome.apprenant
    );
    this.filiereEtudesSharedCollection = this.filiereEtudeService.addFiliereEtudeToCollectionIfMissing(
      this.filiereEtudesSharedCollection,
      diplome.filiereEtude
    );
    this.serieEtudesSharedCollection = this.serieEtudeService.addSerieEtudeToCollectionIfMissing(
      this.serieEtudesSharedCollection,
      diplome.serieEtude
    );
    this.niveauEtudesSharedCollection = this.niveauEtudeService.addNiveauEtudeToCollectionIfMissing(
      this.niveauEtudesSharedCollection,
      diplome.niveauEtude
    );
  }

  protected loadRelationshipsOptions(): void {
    this.apprenantService
      .query()
      .pipe(map((res: HttpResponse<IApprenant[]>) => res.body ?? []))
      .pipe(
        map((apprenants: IApprenant[]) =>
          this.apprenantService.addApprenantToCollectionIfMissing(apprenants, this.editForm.get('apprenant')!.value)
        )
      )
      .subscribe((apprenants: IApprenant[]) => (this.apprenantsSharedCollection = apprenants));

    this.filiereEtudeService
      .query()
      .pipe(map((res: HttpResponse<IFiliereEtude[]>) => res.body ?? []))
      .pipe(
        map((filiereEtudes: IFiliereEtude[]) =>
          this.filiereEtudeService.addFiliereEtudeToCollectionIfMissing(filiereEtudes, this.editForm.get('filiereEtude')!.value)
        )
      )
      .subscribe((filiereEtudes: IFiliereEtude[]) => (this.filiereEtudesSharedCollection = filiereEtudes));

    this.serieEtudeService
      .query()
      .pipe(map((res: HttpResponse<ISerieEtude[]>) => res.body ?? []))
      .pipe(
        map((serieEtudes: ISerieEtude[]) =>
          this.serieEtudeService.addSerieEtudeToCollectionIfMissing(serieEtudes, this.editForm.get('serieEtude')!.value)
        )
      )
      .subscribe((serieEtudes: ISerieEtude[]) => (this.serieEtudesSharedCollection = serieEtudes));

    this.niveauEtudeService
      .query()
      .pipe(map((res: HttpResponse<INiveauEtude[]>) => res.body ?? []))
      .pipe(
        map((niveauEtudes: INiveauEtude[]) =>
          this.niveauEtudeService.addNiveauEtudeToCollectionIfMissing(niveauEtudes, this.editForm.get('niveauEtude')!.value)
        )
      )
      .subscribe((niveauEtudes: INiveauEtude[]) => (this.niveauEtudesSharedCollection = niveauEtudes));
  }

  protected createFromForm(): IDiplome {
    return {
      ...new Diplome(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      matriculeDiplome: this.editForm.get(['matriculeDiplome'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      apprenant: this.editForm.get(['apprenant'])!.value,
      filiereEtude: this.editForm.get(['filiereEtude'])!.value,
      serieEtude: this.editForm.get(['serieEtude'])!.value,
      niveauEtude: this.editForm.get(['niveauEtude'])!.value,
    };
  }
}
