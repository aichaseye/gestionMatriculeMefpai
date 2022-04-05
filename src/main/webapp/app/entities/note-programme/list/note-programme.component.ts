import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INoteProgramme } from '../note-programme.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { NoteProgrammeService } from '../service/note-programme.service';
import { NoteProgrammeDeleteDialogComponent } from '../delete/note-programme-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-note-programme',
  templateUrl: './note-programme.component.html',
})
export class NoteProgrammeComponent implements OnInit {
  noteProgrammes: INoteProgramme[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected noteProgrammeService: NoteProgrammeService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.noteProgrammes = [];
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

    this.noteProgrammeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<INoteProgramme[]>) => {
          this.isLoading = false;
          this.paginateNoteProgrammes(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.noteProgrammes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: INoteProgramme): number {
    return item.id!;
  }

  delete(noteProgramme: INoteProgramme): void {
    const modalRef = this.modalService.open(NoteProgrammeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.noteProgramme = noteProgramme;
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

  protected paginateNoteProgrammes(data: INoteProgramme[] | null, headers: HttpHeaders): void {
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
        this.noteProgrammes.push(d);
      }
    }
  }
}
