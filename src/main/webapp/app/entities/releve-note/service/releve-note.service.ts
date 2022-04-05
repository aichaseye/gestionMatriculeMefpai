import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReleveNote, getReleveNoteIdentifier } from '../releve-note.model';

export type EntityResponseType = HttpResponse<IReleveNote>;
export type EntityArrayResponseType = HttpResponse<IReleveNote[]>;

@Injectable({ providedIn: 'root' })
export class ReleveNoteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/releve-notes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(releveNote: IReleveNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(releveNote);
    return this.http
      .post<IReleveNote>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(releveNote: IReleveNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(releveNote);
    return this.http
      .put<IReleveNote>(`${this.resourceUrl}/${getReleveNoteIdentifier(releveNote) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(releveNote: IReleveNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(releveNote);
    return this.http
      .patch<IReleveNote>(`${this.resourceUrl}/${getReleveNoteIdentifier(releveNote) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReleveNote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReleveNote[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReleveNoteToCollectionIfMissing(
    releveNoteCollection: IReleveNote[],
    ...releveNotesToCheck: (IReleveNote | null | undefined)[]
  ): IReleveNote[] {
    const releveNotes: IReleveNote[] = releveNotesToCheck.filter(isPresent);
    if (releveNotes.length > 0) {
      const releveNoteCollectionIdentifiers = releveNoteCollection.map(releveNoteItem => getReleveNoteIdentifier(releveNoteItem)!);
      const releveNotesToAdd = releveNotes.filter(releveNoteItem => {
        const releveNoteIdentifier = getReleveNoteIdentifier(releveNoteItem);
        if (releveNoteIdentifier == null || releveNoteCollectionIdentifiers.includes(releveNoteIdentifier)) {
          return false;
        }
        releveNoteCollectionIdentifiers.push(releveNoteIdentifier);
        return true;
      });
      return [...releveNotesToAdd, ...releveNoteCollection];
    }
    return releveNoteCollection;
  }

  protected convertDateFromClient(releveNote: IReleveNote): IReleveNote {
    return Object.assign({}, releveNote, {
      annee: releveNote.annee?.isValid() ? releveNote.annee.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.annee = res.body.annee ? dayjs(res.body.annee) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((releveNote: IReleveNote) => {
        releveNote.annee = releveNote.annee ? dayjs(releveNote.annee) : undefined;
      });
    }
    return res;
  }
}
