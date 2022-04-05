import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRegion, Region } from '../region.model';
import { RegionService } from '../service/region.service';
import { IPays } from 'app/entities/pays/pays.model';
import { PaysService } from 'app/entities/pays/service/pays.service';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';

@Component({
  selector: 'jhi-region-update',
  templateUrl: './region-update.component.html',
})
export class RegionUpdateComponent implements OnInit {
  isSaving = false;
  nomRegionValues = Object.keys(NomRegion);

  paysSharedCollection: IPays[] = [];

  editForm = this.fb.group({
    id: [],
    nomReg: [null, [Validators.required]],
    pays: [],
  });

  constructor(
    protected regionService: RegionService,
    protected paysService: PaysService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ region }) => {
      this.updateForm(region);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const region = this.createFromForm();
    if (region.id !== undefined) {
      this.subscribeToSaveResponse(this.regionService.update(region));
    } else {
      this.subscribeToSaveResponse(this.regionService.create(region));
    }
  }

  trackPaysById(index: number, item: IPays): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegion>>): void {
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

  protected updateForm(region: IRegion): void {
    this.editForm.patchValue({
      id: region.id,
      nomReg: region.nomReg,
      pays: region.pays,
    });

    this.paysSharedCollection = this.paysService.addPaysToCollectionIfMissing(this.paysSharedCollection, region.pays);
  }

  protected loadRelationshipsOptions(): void {
    this.paysService
      .query()
      .pipe(map((res: HttpResponse<IPays[]>) => res.body ?? []))
      .pipe(map((pays: IPays[]) => this.paysService.addPaysToCollectionIfMissing(pays, this.editForm.get('pays')!.value)))
      .subscribe((pays: IPays[]) => (this.paysSharedCollection = pays));
  }

  protected createFromForm(): IRegion {
    return {
      ...new Region(),
      id: this.editForm.get(['id'])!.value,
      nomReg: this.editForm.get(['nomReg'])!.value,
      pays: this.editForm.get(['pays'])!.value,
    };
  }
}
