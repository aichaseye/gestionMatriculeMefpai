import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INoteProgramme, NoteProgramme } from '../note-programme.model';
import { NoteProgrammeService } from '../service/note-programme.service';
import { IReleveNote } from 'app/entities/releve-note/releve-note.model';
import { ReleveNoteService } from 'app/entities/releve-note/service/releve-note.service';

@Component({
  selector: 'jhi-note-programme-update',
  templateUrl: './note-programme-update.component.html',
})
export class NoteProgrammeUpdateComponent implements OnInit {
  isSaving = false;

  releveNotesSharedCollection: IReleveNote[] = [];

  editForm = this.fb.group({
    id: [],
    nomProg: [],
    coef: [],
    note: [],
    releveNotes: [],
  });

  constructor(
    protected noteProgrammeService: NoteProgrammeService,
    protected releveNoteService: ReleveNoteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ noteProgramme }) => {
      this.updateForm(noteProgramme);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const noteProgramme = this.createFromForm();
    if (noteProgramme.id !== undefined) {
      this.subscribeToSaveResponse(this.noteProgrammeService.update(noteProgramme));
    } else {
      this.subscribeToSaveResponse(this.noteProgrammeService.create(noteProgramme));
    }
  }

  trackReleveNoteById(index: number, item: IReleveNote): number {
    return item.id!;
  }

  getSelectedReleveNote(option: IReleveNote, selectedVals?: IReleveNote[]): IReleveNote {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INoteProgramme>>): void {
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

  protected updateForm(noteProgramme: INoteProgramme): void {
    this.editForm.patchValue({
      id: noteProgramme.id,
      nomProg: noteProgramme.nomProg,
      coef: noteProgramme.coef,
      note: noteProgramme.note,
      releveNotes: noteProgramme.releveNotes,
    });

    this.releveNotesSharedCollection = this.releveNoteService.addReleveNoteToCollectionIfMissing(
      this.releveNotesSharedCollection,
      ...(noteProgramme.releveNotes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.releveNoteService
      .query()
      .pipe(map((res: HttpResponse<IReleveNote[]>) => res.body ?? []))
      .pipe(
        map((releveNotes: IReleveNote[]) =>
          this.releveNoteService.addReleveNoteToCollectionIfMissing(releveNotes, ...(this.editForm.get('releveNotes')!.value ?? []))
        )
      )
      .subscribe((releveNotes: IReleveNote[]) => (this.releveNotesSharedCollection = releveNotes));
  }

  protected createFromForm(): INoteProgramme {
    return {
      ...new NoteProgramme(),
      id: this.editForm.get(['id'])!.value,
      nomProg: this.editForm.get(['nomProg'])!.value,
      coef: this.editForm.get(['coef'])!.value,
      note: this.editForm.get(['note'])!.value,
      releveNotes: this.editForm.get(['releveNotes'])!.value,
    };
  }
}
