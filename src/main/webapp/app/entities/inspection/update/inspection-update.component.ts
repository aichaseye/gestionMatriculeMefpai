import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInspection, Inspection } from '../inspection.model';
import { InspectionService } from '../service/inspection.service';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';
import { TypeInspection } from 'app/entities/enumerations/type-inspection.model';

@Component({
  selector: 'jhi-inspection-update',
  templateUrl: './inspection-update.component.html',
})
export class InspectionUpdateComponent implements OnInit {
  isSaving = false;
  typeInspectionValues = Object.keys(TypeInspection);

  communesSharedCollection: ICommune[] = [];

  editForm = this.fb.group({
    id: [],
    nomInsp: [null, [Validators.required]],
    typeInsp: [null, [Validators.required]],
    latitude: [],
    longitude: [],
    commune: [],
  });

  constructor(
    protected inspectionService: InspectionService,
    protected communeService: CommuneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspection }) => {
      this.updateForm(inspection);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inspection = this.createFromForm();
    if (inspection.id !== undefined) {
      this.subscribeToSaveResponse(this.inspectionService.update(inspection));
    } else {
      this.subscribeToSaveResponse(this.inspectionService.create(inspection));
    }
  }

  trackCommuneById(index: number, item: ICommune): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspection>>): void {
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

  protected updateForm(inspection: IInspection): void {
    this.editForm.patchValue({
      id: inspection.id,
      nomInsp: inspection.nomInsp,
      typeInsp: inspection.typeInsp,
      latitude: inspection.latitude,
      longitude: inspection.longitude,
      commune: inspection.commune,
    });

    this.communesSharedCollection = this.communeService.addCommuneToCollectionIfMissing(this.communesSharedCollection, inspection.commune);
  }

  protected loadRelationshipsOptions(): void {
    this.communeService
      .query()
      .pipe(map((res: HttpResponse<ICommune[]>) => res.body ?? []))
      .pipe(
        map((communes: ICommune[]) => this.communeService.addCommuneToCollectionIfMissing(communes, this.editForm.get('commune')!.value))
      )
      .subscribe((communes: ICommune[]) => (this.communesSharedCollection = communes));
  }

  protected createFromForm(): IInspection {
    return {
      ...new Inspection(),
      id: this.editForm.get(['id'])!.value,
      nomInsp: this.editForm.get(['nomInsp'])!.value,
      typeInsp: this.editForm.get(['typeInsp'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      commune: this.editForm.get(['commune'])!.value,
    };
  }
}
