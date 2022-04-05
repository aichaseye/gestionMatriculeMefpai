import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInscription, Inscription } from '../inscription.model';
import { InscriptionService } from '../service/inscription.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IClasse } from 'app/entities/classe/classe.model';
import { ClasseService } from 'app/entities/classe/service/classe.service';

@Component({
  selector: 'jhi-inscription-update',
  templateUrl: './inscription-update.component.html',
})
export class InscriptionUpdateComponent implements OnInit {
  isSaving = false;

  apprenantsSharedCollection: IApprenant[] = [];
  classesSharedCollection: IClasse[] = [];

  editForm = this.fb.group({
    id: [],
    anneeIns: [null, [Validators.required]],
    apprenant: [],
    classe: [],
  });

  constructor(
    protected inscriptionService: InscriptionService,
    protected apprenantService: ApprenantService,
    protected classeService: ClasseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscription }) => {
      this.updateForm(inscription);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscription = this.createFromForm();
    if (inscription.id !== undefined) {
      this.subscribeToSaveResponse(this.inscriptionService.update(inscription));
    } else {
      this.subscribeToSaveResponse(this.inscriptionService.create(inscription));
    }
  }

  trackApprenantById(index: number, item: IApprenant): number {
    return item.id!;
  }

  trackClasseById(index: number, item: IClasse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscription>>): void {
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

  protected updateForm(inscription: IInscription): void {
    this.editForm.patchValue({
      id: inscription.id,
      anneeIns: inscription.anneeIns,
      apprenant: inscription.apprenant,
      classe: inscription.classe,
    });

    this.apprenantsSharedCollection = this.apprenantService.addApprenantToCollectionIfMissing(
      this.apprenantsSharedCollection,
      inscription.apprenant
    );
    this.classesSharedCollection = this.classeService.addClasseToCollectionIfMissing(this.classesSharedCollection, inscription.classe);
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

    this.classeService
      .query()
      .pipe(map((res: HttpResponse<IClasse[]>) => res.body ?? []))
      .pipe(map((classes: IClasse[]) => this.classeService.addClasseToCollectionIfMissing(classes, this.editForm.get('classe')!.value)))
      .subscribe((classes: IClasse[]) => (this.classesSharedCollection = classes));
  }

  protected createFromForm(): IInscription {
    return {
      ...new Inscription(),
      id: this.editForm.get(['id'])!.value,
      anneeIns: this.editForm.get(['anneeIns'])!.value,
      apprenant: this.editForm.get(['apprenant'])!.value,
      classe: this.editForm.get(['classe'])!.value,
    };
  }
}
