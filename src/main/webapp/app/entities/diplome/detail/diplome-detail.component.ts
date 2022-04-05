import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDiplome } from '../diplome.model';

@Component({
  selector: 'jhi-diplome-detail',
  templateUrl: './diplome-detail.component.html',
})
export class DiplomeDetailComponent implements OnInit {
  diplome: IDiplome | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ diplome }) => {
      this.diplome = diplome;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
