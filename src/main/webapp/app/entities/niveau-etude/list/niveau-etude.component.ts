import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INiveauEtude } from '../niveau-etude.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { NiveauEtudeService } from '../service/niveau-etude.service';
import { NiveauEtudeDeleteDialogComponent } from '../delete/niveau-etude-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-niveau-etude',
  templateUrl: './niveau-etude.component.html',
})
export class NiveauEtudeComponent implements OnInit {
  niveauEtudes: INiveauEtude[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected niveauEtudeService: NiveauEtudeService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.niveauEtudes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.niveauEtudeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<INiveauEtude[]>) => {
          this.isLoading = false;
          this.paginateNiveauEtudes(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.niveauEtudes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: INiveauEtude): number {
    return item.id!;
  }

  delete(niveauEtude: INiveauEtude): void {
    const modalRef = this.modalService.open(NiveauEtudeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.niveauEtude = niveauEtude;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateNiveauEtudes(data: INiveauEtude[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.niveauEtudes.push(d);
      }
    }
  }
}
