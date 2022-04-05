import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFiliereEtude } from '../filiere-etude.model';

@Component({
  selector: 'jhi-filiere-etude-detail',
  templateUrl: './filiere-etude-detail.component.html',
})
export class FiliereEtudeDetailComponent implements OnInit {
  filiereEtude: IFiliereEtude | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filiereEtude }) => {
      this.filiereEtude = filiereEtude;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
