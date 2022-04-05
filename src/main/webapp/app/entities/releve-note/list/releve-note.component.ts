import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReleveNote } from '../releve-note.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ReleveNoteService } from '../service/releve-note.service';
import { ReleveNoteDeleteDialogComponent } from '../delete/releve-note-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-releve-note',
  templateUrl: './releve-note.component.html',
})
export class ReleveNoteComponent implements OnInit {
  releveNotes: IReleveNote[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected releveNoteService: ReleveNoteService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.releveNotes = [];
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

    this.releveNoteService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IReleveNote[]>) => {
          this.isLoading = false;
          this.paginateReleveNotes(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.releveNotes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IReleveNote): number {
    return item.id!;
  }

  delete(releveNote: IReleveNote): void {
    const modalRef = this.modalService.open(ReleveNoteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.releveNote = releveNote;
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

  protected paginateReleveNotes(data: IReleveNote[] | null, headers: HttpHeaders): void {
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
        this.releveNotes.push(d);
      }
    }
  }
}
