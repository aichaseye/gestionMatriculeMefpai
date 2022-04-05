import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INiveauEtude } from '../niveau-etude.model';

@Component({
  selector: 'jhi-niveau-etude-detail',
  templateUrl: './niveau-etude-detail.component.html',
})
export class NiveauEtudeDetailComponent implements OnInit {
  niveauEtude: INiveauEtude | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ niveauEtude }) => {
      this.niveauEtude = niveauEtude;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
