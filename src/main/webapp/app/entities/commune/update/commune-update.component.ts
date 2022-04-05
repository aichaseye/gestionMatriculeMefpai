import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommune, Commune } from '../commune.model';
import { CommuneService } from '../service/commune.service';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';

@Component({
  selector: 'jhi-commune-update',
  templateUrl: './commune-update.component.html',
})
export class CommuneUpdateComponent implements OnInit {
  isSaving = false;

  departementsSharedCollection: IDepartement[] = [];

  editForm = this.fb.group({
    id: [],
    nomCom: [null, [Validators.required]],
    departement: [],
  });

  constructor(
    protected communeService: CommuneService,
    protected departementService: DepartementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commune }) => {
      this.updateForm(commune);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commune = this.createFromForm();
    if (commune.id !== undefined) {
      this.subscribeToSaveResponse(this.communeService.update(commune));
    } else {
      this.subscribeToSaveResponse(this.communeService.create(commune));
    }
  }

  trackDepartementById(index: number, item: IDepartement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommune>>): void {
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

  protected updateForm(commune: ICommune): void {
    this.editForm.patchValue({
      id: commune.id,
      nomCom: commune.nomCom,
      departement: commune.departement,
    });

    this.departementsSharedCollection = this.departementService.addDepartementToCollectionIfMissing(
      this.departementsSharedCollection,
      commune.departement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departementService
      .query()
      .pipe(map((res: HttpResponse<IDepartement[]>) => res.body ?? []))
      .pipe(
        map((departements: IDepartement[]) =>
          this.departementService.addDepartementToCollectionIfMissing(departements, this.editForm.get('departement')!.value)
        )
      )
      .subscribe((departements: IDepartement[]) => (this.departementsSharedCollection = departements));
  }

  protected createFromForm(): ICommune {
    return {
      ...new Commune(),
      id: this.editForm.get(['id'])!.value,
      nomCom: this.editForm.get(['nomCom'])!.value,
      departement: this.editForm.get(['departement'])!.value,
    };
  }
}
