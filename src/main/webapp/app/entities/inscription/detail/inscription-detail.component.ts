import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInscription } from '../inscription.model';

@Component({
  selector: 'jhi-inscription-detail',
  templateUrl: './inscription-detail.component.html',
})
export class InscriptionDetailComponent implements OnInit {
  inscription: IInscription | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscription }) => {
      this.inscription = inscription;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
