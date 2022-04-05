import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBon } from '../bon.model';

@Component({
  selector: 'jhi-bon-detail',
  templateUrl: './bon-detail.component.html',
})
export class BonDetailComponent implements OnInit {
  bon: IBon | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bon }) => {
      this.bon = bon;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
