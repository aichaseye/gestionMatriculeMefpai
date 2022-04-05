import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApprenant, Apprenant } from '../apprenant.model';
import { ApprenantService } from '../service/apprenant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDemandeMatApp } from 'app/entities/demande-mat-app/demande-mat-app.model';
import { DemandeMatAppService } from 'app/entities/demande-mat-app/service/demande-mat-app.service';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { StatutApp } from 'app/entities/enumerations/statut-app.model';

@Component({
  selector: 'jhi-apprenant-update',
  templateUrl: './apprenant-update.component.html',
})
export class ApprenantUpdateComponent implements OnInit {
  isSaving = false;
  sexeValues = Object.keys(Sexe);
  statutAppValues = Object.keys(StatutApp);

  demandeMatAppsCollection: IDemandeMatApp[] = [];

  editForm = this.fb.group({
    id: [],
    nomCompletApp: [null, [Validators.required]],
    email: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    dateNaissance: [],
    lieuNaissance: [],
    numActeNaissance: [],
    photo: [],
    photoContentType: [],
    sexe: [null, [Validators.required]],
    adresse: [null, [Validators.required]],
    statut: [],
    situationMatrimoniale: [],
    matriculeApp: [],
    nationalite: [null, [Validators.required]],
    demandeMatApp: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected apprenantService: ApprenantService,
    protected demandeMatAppService: DemandeMatAppService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apprenant }) => {
      this.updateForm(apprenant);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('matriculeEfptBdApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apprenant = this.createFromForm();
    if (apprenant.id !== undefined) {
      this.subscribeToSaveResponse(this.apprenantService.update(apprenant));
    } else {
      this.subscribeToSaveResponse(this.apprenantService.create(apprenant));
    }
  }

  trackDemandeMatAppById(index: number, item: IDemandeMatApp): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApprenant>>): void {
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

  protected updateForm(apprenant: IApprenant): void {
    this.editForm.patchValue({
      id: apprenant.id,
      nomCompletApp: apprenant.nomCompletApp,
      email: apprenant.email,
      telephone: apprenant.telephone,
      dateNaissance: apprenant.dateNaissance,
      lieuNaissance: apprenant.lieuNaissance,
      numActeNaissance: apprenant.numActeNaissance,
      photo: apprenant.photo,
      photoContentType: apprenant.photoContentType,
      sexe: apprenant.sexe,
      adresse: apprenant.adresse,
      statut: apprenant.statut,
      situationMatrimoniale: apprenant.situationMatrimoniale,
      matriculeApp: apprenant.matriculeApp,
      nationalite: apprenant.nationalite,
      demandeMatApp: apprenant.demandeMatApp,
    });

    this.demandeMatAppsCollection = this.demandeMatAppService.addDemandeMatAppToCollectionIfMissing(
      this.demandeMatAppsCollection,
      apprenant.demandeMatApp
    );
  }

  protected loadRelationshipsOptions(): void {
    this.demandeMatAppService
      .query({ filter: 'apprenant-is-null' })
      .pipe(map((res: HttpResponse<IDemandeMatApp[]>) => res.body ?? []))
      .pipe(
        map((demandeMatApps: IDemandeMatApp[]) =>
          this.demandeMatAppService.addDemandeMatAppToCollectionIfMissing(demandeMatApps, this.editForm.get('demandeMatApp')!.value)
        )
      )
      .subscribe((demandeMatApps: IDemandeMatApp[]) => (this.demandeMatAppsCollection = demandeMatApps));
  }

  protected createFromForm(): IApprenant {
    return {
      ...new Apprenant(),
      id: this.editForm.get(['id'])!.value,
      nomCompletApp: this.editForm.get(['nomCompletApp'])!.value,
      email: this.editForm.get(['email'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value,
      lieuNaissance: this.editForm.get(['lieuNaissance'])!.value,
      numActeNaissance: this.editForm.get(['numActeNaissance'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      situationMatrimoniale: this.editForm.get(['situationMatrimoniale'])!.value,
      matriculeApp: this.editForm.get(['matriculeApp'])!.value,
      nationalite: this.editForm.get(['nationalite'])!.value,
      demandeMatApp: this.editForm.get(['demandeMatApp'])!.value,
    };
  }
}
