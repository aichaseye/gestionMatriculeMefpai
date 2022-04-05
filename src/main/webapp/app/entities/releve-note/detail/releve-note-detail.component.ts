import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReleveNote } from '../releve-note.model';

@Component({
  selector: 'jhi-releve-note-detail',
  templateUrl: './releve-note-detail.component.html',
})
export class ReleveNoteDetailComponent implements OnInit {
  releveNote: IReleveNote | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releveNote }) => {
      this.releveNote = releveNote;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
