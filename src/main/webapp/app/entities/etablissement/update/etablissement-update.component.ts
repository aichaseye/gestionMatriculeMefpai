import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEtablissement, Etablissement } from '../etablissement.model';
import { EtablissementService } from '../service/etablissement.service';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';
import { IInspection } from 'app/entities/inspection/inspection.model';
import { InspectionService } from 'app/entities/inspection/service/inspection.service';
import { TypeEtab } from 'app/entities/enumerations/type-etab.model';
import { StatutEtab } from 'app/entities/enumerations/statut-etab.model';

@Component({
  selector: 'jhi-etablissement-update',
  templateUrl: './etablissement-update.component.html',
})
export class EtablissementUpdateComponent implements OnInit {
  isSaving = false;
  typeEtabValues = Object.keys(TypeEtab);
  statutEtabValues = Object.keys(StatutEtab);

  communesSharedCollection: ICommune[] = [];
  inspectionsSharedCollection: IInspection[] = [];

  editForm = this.fb.group({
    id: [],
    nomEtab: [null, [Validators.required]],
    typeEtab: [null, [Validators.required]],
    statut: [],
    latitude: [],
    longitude: [],
    matriculeEtab: [],
    commune: [],
    inspection: [],
  });

  constructor(
    protected etablissementService: EtablissementService,
    protected communeService: CommuneService,
    protected inspectionService: InspectionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.updateForm(etablissement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etablissement = this.createFromForm();
    if (etablissement.id !== undefined) {
      this.subscribeToSaveResponse(this.etablissementService.update(etablissement));
    } else {
      this.subscribeToSaveResponse(this.etablissementService.create(etablissement));
    }
  }

  trackCommuneById(index: number, item: ICommune): number {
    return item.id!;
  }

  trackInspectionById(index: number, item: IInspection): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtablissement>>): void {
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

  protected updateForm(etablissement: IEtablissement): void {
    this.editForm.patchValue({
      id: etablissement.id,
      nomEtab: etablissement.nomEtab,
      typeEtab: etablissement.typeEtab,
      statut: etablissement.statut,
      latitude: etablissement.latitude,
      longitude: etablissement.longitude,
      matriculeEtab: etablissement.matriculeEtab,
      commune: etablissement.commune,
      inspection: etablissement.inspection,
    });

    this.communesSharedCollection = this.communeService.addCommuneToCollectionIfMissing(
      this.communesSharedCollection,
      etablissement.commune
    );
    this.inspectionsSharedCollection = this.inspectionService.addInspectionToCollectionIfMissing(
      this.inspectionsSharedCollection,
      etablissement.inspection
    );
  }

  protected loadRelationshipsOptions(): void {
    this.communeService
      .query()
      .pipe(map((res: HttpResponse<ICommune[]>) => res.body ?? []))
      .pipe(
        map((communes: ICommune[]) => this.communeService.addCommuneToCollectionIfMissing(communes, this.editForm.get('commune')!.value))
      )
      .subscribe((communes: ICommune[]) => (this.communesSharedCollection = communes));

    this.inspectionService
      .query()
      .pipe(map((res: HttpResponse<IInspection[]>) => res.body ?? []))
      .pipe(
        map((inspections: IInspection[]) =>
          this.inspectionService.addInspectionToCollectionIfMissing(inspections, this.editForm.get('inspection')!.value)
        )
      )
      .subscribe((inspections: IInspection[]) => (this.inspectionsSharedCollection = inspections));
  }

  protected createFromForm(): IEtablissement {
    return {
      ...new Etablissement(),
      id: this.editForm.get(['id'])!.value,
      nomEtab: this.editForm.get(['nomEtab'])!.value,
      typeEtab: this.editForm.get(['typeEtab'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      matriculeEtab: this.editForm.get(['matriculeEtab'])!.value,
      commune: this.editForm.get(['commune'])!.value,
      inspection: this.editForm.get(['inspection'])!.value,
    };
  }
}
