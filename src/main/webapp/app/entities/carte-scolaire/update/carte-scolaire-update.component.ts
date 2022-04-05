import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICarteScolaire, CarteScolaire } from '../carte-scolaire.model';
import { CarteScolaireService } from '../service/carte-scolaire.service';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';

@Component({
  selector: 'jhi-carte-scolaire-update',
  templateUrl: './carte-scolaire-update.component.html',
})
export class CarteScolaireUpdateComponent implements OnInit {
  isSaving = false;

  apprenantsCollection: IApprenant[] = [];

  editForm = this.fb.group({
    id: [],
    longuer: [null, [Validators.required]],
    largeur: [null, [Validators.required]],
    dureeValidite: [],
    dateCreation: [],
    dateFin: [],
    matriculeCart: [],
    apprenant: [],
  });

  constructor(
    protected carteScolaireService: CarteScolaireService,
    protected apprenantService: ApprenantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carteScolaire }) => {
      this.updateForm(carteScolaire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const carteScolaire = this.createFromForm();
    if (carteScolaire.id !== undefined) {
      this.subscribeToSaveResponse(this.carteScolaireService.update(carteScolaire));
    } else {
      this.subscribeToSaveResponse(this.carteScolaireService.create(carteScolaire));
    }
  }

  trackApprenantById(index: number, item: IApprenant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarteScolaire>>): void {
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

  protected updateForm(carteScolaire: ICarteScolaire): void {
    this.editForm.patchValue({
      id: carteScolaire.id,
      longuer: carteScolaire.longuer,
      largeur: carteScolaire.largeur,
      dureeValidite: carteScolaire.dureeValidite,
      dateCreation: carteScolaire.dateCreation,
      dateFin: carteScolaire.dateFin,
      matriculeCart: carteScolaire.matriculeCart,
      apprenant: carteScolaire.apprenant,
    });

    this.apprenantsCollection = this.apprenantService.addApprenantToCollectionIfMissing(this.apprenantsCollection, carteScolaire.apprenant);
  }

  protected loadRelationshipsOptions(): void {
    this.apprenantService
      .query({ filter: 'cartescolaire-is-null' })
      .pipe(map((res: HttpResponse<IApprenant[]>) => res.body ?? []))
      .pipe(
        map((apprenants: IApprenant[]) =>
          this.apprenantService.addApprenantToCollectionIfMissing(apprenants, this.editForm.get('apprenant')!.value)
        )
      )
      .subscribe((apprenants: IApprenant[]) => (this.apprenantsCollection = apprenants));
  }

  protected createFromForm(): ICarteScolaire {
    return {
      ...new CarteScolaire(),
      id: this.editForm.get(['id'])!.value,
      longuer: this.editForm.get(['longuer'])!.value,
      largeur: this.editForm.get(['largeur'])!.value,
      dureeValidite: this.editForm.get(['dureeValidite'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateFin: this.editForm.get(['dateFin'])!.value,
      matriculeCart: this.editForm.get(['matriculeCart'])!.value,
      apprenant: this.editForm.get(['apprenant'])!.value,
    };
  }
}
