import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonnel } from '../personnel.model';

@Component({
  selector: 'jhi-personnel-detail',
  templateUrl: './personnel-detail.component.html',
})
export class PersonnelDetailComponent implements OnInit {
  personnel: IPersonnel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personnel }) => {
      this.personnel = personnel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
