import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReleveNote, ReleveNote } from '../releve-note.model';
import { ReleveNoteService } from '../service/releve-note.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IFiliereEtude } from 'app/entities/filiere-etude/filiere-etude.model';
import { FiliereEtudeService } from 'app/entities/filiere-etude/service/filiere-etude.service';
import { ISerieEtude } from 'app/entities/serie-etude/serie-etude.model';
import { SerieEtudeService } from 'app/entities/serie-etude/service/serie-etude.service';
import { INiveauEtude } from 'app/entities/niveau-etude/niveau-etude.model';
import { NiveauEtudeService } from 'app/entities/niveau-etude/service/niveau-etude.service';
import { Etat } from 'app/entities/enumerations/etat.model';
import { NomSemestre } from 'app/entities/enumerations/nom-semestre.model';

@Component({
  selector: 'jhi-releve-note-update',
  templateUrl: './releve-note-update.component.html',
})
export class ReleveNoteUpdateComponent implements OnInit {
  isSaving = false;
  etatValues = Object.keys(Etat);
  nomSemestreValues = Object.keys(NomSemestre);

  apprenantsSharedCollection: IApprenant[] = [];
  filiereEtudesSharedCollection: IFiliereEtude[] = [];
  serieEtudesSharedCollection: ISerieEtude[] = [];
  niveauEtudesSharedCollection: INiveauEtude[] = [];

  editForm = this.fb.group({
    id: [],
    annee: [null, [Validators.required]],
    etat: [null, [Validators.required]],
    apreciation: [],
    moyenne: [null, [Validators.required]],
    moyenneGenerale: [],
    rang: [],
    noteConduite: [],
    nomSemestre: [],
    matriculeRel: [],
    apprenant: [],
    filiereEtude: [],
    serieEtude: [],
    niveauEtude: [],
  });

  constructor(
    protected releveNoteService: ReleveNoteService,
    protected apprenantService: ApprenantService,
    protected filiereEtudeService: FiliereEtudeService,
    protected serieEtudeService: SerieEtudeService,
    protected niveauEtudeService: NiveauEtudeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releveNote }) => {
      this.updateForm(releveNote);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const releveNote = this.createFromForm();
    if (releveNote.id !== undefined) {
      this.subscribeToSaveResponse(this.releveNoteService.update(releveNote));
    } else {
      this.subscribeToSaveResponse(this.releveNoteService.create(releveNote));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReleveNote>>): void {
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

  protected updateForm(releveNote: IReleveNote): void {
    this.editForm.patchValue({
      id: releveNote.id,
      annee: releveNote.annee,
      etat: releveNote.etat,
      apreciation: releveNote.apreciation,
      moyenne: releveNote.moyenne,
      moyenneGenerale: releveNote.moyenneGenerale,
      rang: releveNote.rang,
      noteConduite: releveNote.noteConduite,
      nomSemestre: releveNote.nomSemestre,
      matriculeRel: releveNote.matriculeRel,
      apprenant: releveNote.apprenant,
      filiereEtude: releveNote.filiereEtude,
      serieEtude: releveNote.serieEtude,
      niveauEtude: releveNote.niveauEtude,
    });

    this.apprenantsSharedCollection = this.apprenantService.addApprenantToCollectionIfMissing(
      this.apprenantsSharedCollection,
      releveNote.apprenant
    );
    this.filiereEtudesSharedCollection = this.filiereEtudeService.addFiliereEtudeToCollectionIfMissing(
      this.filiereEtudesSharedCollection,
      releveNote.filiereEtude
    );
    this.serieEtudesSharedCollection = this.serieEtudeService.addSerieEtudeToCollectionIfMissing(
      this.serieEtudesSharedCollection,
      releveNote.serieEtude
    );
    this.niveauEtudesSharedCollection = this.niveauEtudeService.addNiveauEtudeToCollectionIfMissing(
      this.niveauEtudesSharedCollection,
      releveNote.niveauEtude
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

  protected createFromForm(): IReleveNote {
    return {
      ...new ReleveNote(),
      id: this.editForm.get(['id'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      etat: this.editForm.get(['etat'])!.value,
      apreciation: this.editForm.get(['apreciation'])!.value,
      moyenne: this.editForm.get(['moyenne'])!.value,
      moyenneGenerale: this.editForm.get(['moyenneGenerale'])!.value,
      rang: this.editForm.get(['rang'])!.value,
      noteConduite: this.editForm.get(['noteConduite'])!.value,
      nomSemestre: this.editForm.get(['nomSemestre'])!.value,
      matriculeRel: this.editForm.get(['matriculeRel'])!.value,
      apprenant: this.editForm.get(['apprenant'])!.value,
      filiereEtude: this.editForm.get(['filiereEtude'])!.value,
      serieEtude: this.editForm.get(['serieEtude'])!.value,
      niveauEtude: this.editForm.get(['niveauEtude'])!.value,
    };
  }
}
