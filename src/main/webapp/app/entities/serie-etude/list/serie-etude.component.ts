import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISerieEtude } from '../serie-etude.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SerieEtudeService } from '../service/serie-etude.service';
import { SerieEtudeDeleteDialogComponent } from '../delete/serie-etude-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-serie-etude',
  templateUrl: './serie-etude.component.html',
})
export class SerieEtudeComponent implements OnInit {
  serieEtudes: ISerieEtude[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected serieEtudeService: SerieEtudeService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.serieEtudes = [];
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

    this.serieEtudeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ISerieEtude[]>) => {
          this.isLoading = false;
          this.paginateSerieEtudes(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.serieEtudes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISerieEtude): number {
    return item.id!;
  }

  delete(serieEtude: ISerieEtude): void {
    const modalRef = this.modalService.open(SerieEtudeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.serieEtude = serieEtude;
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

  protected paginateSerieEtudes(data: ISerieEtude[] | null, headers: HttpHeaders): void {
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
        this.serieEtudes.push(d);
      }
    }
  }
}
