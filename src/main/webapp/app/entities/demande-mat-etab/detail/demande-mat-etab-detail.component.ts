import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemandeMatEtab } from '../demande-mat-etab.model';

@Component({
  selector: 'jhi-demande-mat-etab-detail',
  templateUrl: './demande-mat-etab-detail.component.html',
})
export class DemandeMatEtabDetailComponent implements OnInit {
  demandeMatEtab: IDemandeMatEtab | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeMatEtab }) => {
      this.demandeMatEtab = demandeMatEtab;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
