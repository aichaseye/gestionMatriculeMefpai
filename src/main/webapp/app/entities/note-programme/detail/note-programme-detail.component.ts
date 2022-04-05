import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INoteProgramme } from '../note-programme.model';

@Component({
  selector: 'jhi-note-programme-detail',
  templateUrl: './note-programme-detail.component.html',
})
export class NoteProgrammeDetailComponent implements OnInit {
  noteProgramme: INoteProgramme | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ noteProgramme }) => {
      this.noteProgramme = noteProgramme;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
