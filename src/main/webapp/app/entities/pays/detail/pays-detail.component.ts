import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPays } from '../pays.model';

@Component({
  selector: 'jhi-pays-detail',
  templateUrl: './pays-detail.component.html',
})
export class PaysDetailComponent implements OnInit {
  pays: IPays | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pays }) => {
      this.pays = pays;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
