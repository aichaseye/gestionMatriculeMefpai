import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeMatApp } from '../demande-mat-app.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DemandeMatAppService } from '../service/demande-mat-app.service';
import { DemandeMatAppDeleteDialogComponent } from '../delete/demande-mat-app-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-demande-mat-app',
  templateUrl: './demande-mat-app.component.html',
})
export class DemandeMatAppComponent implements OnInit {
  demandeMatApps: IDemandeMatApp[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected demandeMatAppService: DemandeMatAppService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.demandeMatApps = [];
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

    this.demandeMatAppService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IDemandeMatApp[]>) => {
          this.isLoading = false;
          this.paginateDemandeMatApps(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.demandeMatApps = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDemandeMatApp): number {
    return item.id!;
  }

  delete(demandeMatApp: IDemandeMatApp): void {
    const modalRef = this.modalService.open(DemandeMatAppDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.demandeMatApp = demandeMatApp;
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

  protected paginateDemandeMatApps(data: IDemandeMatApp[] | null, headers: HttpHeaders): void {
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
        this.demandeMatApps.push(d);
      }
    }
  }
}
