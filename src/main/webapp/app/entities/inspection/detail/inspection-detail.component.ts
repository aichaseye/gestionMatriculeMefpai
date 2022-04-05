import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInspection } from '../inspection.model';

@Component({
  selector: 'jhi-inspection-detail',
  templateUrl: './inspection-detail.component.html',
})
export class InspectionDetailComponent implements OnInit {
  inspection: IInspection | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspection }) => {
      this.inspection = inspection;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
