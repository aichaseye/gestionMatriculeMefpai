import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeMatEtab } from '../demande-mat-etab.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DemandeMatEtabService } from '../service/demande-mat-etab.service';
import { DemandeMatEtabDeleteDialogComponent } from '../delete/demande-mat-etab-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-demande-mat-etab',
  templateUrl: './demande-mat-etab.component.html',
})
export class DemandeMatEtabComponent implements OnInit {
  demandeMatEtabs: IDemandeMatEtab[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected demandeMatEtabService: DemandeMatEtabService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.demandeMatEtabs = [];
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

    this.demandeMatEtabService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IDemandeMatEtab[]>) => {
          this.isLoading = false;
          this.paginateDemandeMatEtabs(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.demandeMatEtabs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDemandeMatEtab): number {
    return item.id!;
  }

  delete(demandeMatEtab: IDemandeMatEtab): void {
    const modalRef = this.modalService.open(DemandeMatEtabDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.demandeMatEtab = demandeMatEtab;
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

  protected paginateDemandeMatEtabs(data: IDemandeMatEtab[] | null, headers: HttpHeaders): void {
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
        this.demandeMatEtabs.push(d);
      }
    }
  }
}
