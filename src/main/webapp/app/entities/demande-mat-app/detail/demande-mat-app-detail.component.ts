import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemandeMatApp } from '../demande-mat-app.model';

@Component({
  selector: 'jhi-demande-mat-app-detail',
  templateUrl: './demande-mat-app-detail.component.html',
})
export class DemandeMatAppDetailComponent implements OnInit {
  demandeMatApp: IDemandeMatApp | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeMatApp }) => {
      this.demandeMatApp = demandeMatApp;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
