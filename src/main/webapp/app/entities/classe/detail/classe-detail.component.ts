import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClasse } from '../classe.model';

@Component({
  selector: 'jhi-classe-detail',
  templateUrl: './classe-detail.component.html',
})
export class ClasseDetailComponent implements OnInit {
  classe: IClasse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classe }) => {
      this.classe = classe;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
