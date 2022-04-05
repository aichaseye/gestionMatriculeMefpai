import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommune } from '../commune.model';

@Component({
  selector: 'jhi-commune-detail',
  templateUrl: './commune-detail.component.html',
})
export class CommuneDetailComponent implements OnInit {
  commune: ICommune | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commune }) => {
      this.commune = commune;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
