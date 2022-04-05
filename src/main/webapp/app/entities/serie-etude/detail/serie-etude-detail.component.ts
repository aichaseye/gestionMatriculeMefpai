import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISerieEtude } from '../serie-etude.model';

@Component({
  selector: 'jhi-serie-etude-detail',
  templateUrl: './serie-etude-detail.component.html',
})
export class SerieEtudeDetailComponent implements OnInit {
  serieEtude: ISerieEtude | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serieEtude }) => {
      this.serieEtude = serieEtude;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
