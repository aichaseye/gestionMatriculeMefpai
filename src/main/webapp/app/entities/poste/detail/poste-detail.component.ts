import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPoste } from '../poste.model';

@Component({
  selector: 'jhi-poste-detail',
  templateUrl: './poste-detail.component.html',
})
export class PosteDetailComponent implements OnInit {
  poste: IPoste | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ poste }) => {
      this.poste = poste;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
