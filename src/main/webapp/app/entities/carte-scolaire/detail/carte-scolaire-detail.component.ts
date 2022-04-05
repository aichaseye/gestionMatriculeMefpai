import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICarteScolaire } from '../carte-scolaire.model';

@Component({
  selector: 'jhi-carte-scolaire-detail',
  templateUrl: './carte-scolaire-detail.component.html',
})
export class CarteScolaireDetailComponent implements OnInit {
  carteScolaire: ICarteScolaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carteScolaire }) => {
      this.carteScolaire = carteScolaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
