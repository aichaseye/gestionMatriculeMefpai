import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INoteProgramme, getNoteProgrammeIdentifier } from '../note-programme.model';

export type EntityResponseType = HttpResponse<INoteProgramme>;
export type EntityArrayResponseType = HttpResponse<INoteProgramme[]>;

@Injectable({ providedIn: 'root' })
export class NoteProgrammeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/note-programmes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(noteProgramme: INoteProgramme): Observable<EntityResponseType> {
    return this.http.post<INoteProgramme>(this.resourceUrl, noteProgramme, { observe: 'response' });
  }

  update(noteProgramme: INoteProgramme): Observable<EntityResponseType> {
    return this.http.put<INoteProgramme>(`${this.resourceUrl}/${getNoteProgrammeIdentifier(noteProgramme) as number}`, noteProgramme, {
      observe: 'response',
    });
  }

  partialUpdate(noteProgramme: INoteProgramme): Observable<EntityResponseType> {
    return this.http.patch<INoteProgramme>(`${this.resourceUrl}/${getNoteProgrammeIdentifier(noteProgramme) as number}`, noteProgramme, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INoteProgramme>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INoteProgramme[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNoteProgrammeToCollectionIfMissing(
    noteProgrammeCollection: INoteProgramme[],
    ...noteProgrammesToCheck: (INoteProgramme | null | undefined)[]
  ): INoteProgramme[] {
    const noteProgrammes: INoteProgramme[] = noteProgrammesToCheck.filter(isPresent);
    if (noteProgrammes.length > 0) {
      const noteProgrammeCollectionIdentifiers = noteProgrammeCollection.map(
        noteProgrammeItem => getNoteProgrammeIdentifier(noteProgrammeItem)!
      );
      const noteProgrammesToAdd = noteProgrammes.filter(noteProgrammeItem => {
        const noteProgrammeIdentifier = getNoteProgrammeIdentifier(noteProgrammeItem);
        if (noteProgrammeIdentifier == null || noteProgrammeCollectionIdentifiers.includes(noteProgrammeIdentifier)) {
          return false;
        }
        noteProgrammeCollectionIdentifiers.push(noteProgrammeIdentifier);
        return true;
      });
      return [...noteProgrammesToAdd, ...noteProgrammeCollection];
    }
    return noteProgrammeCollection;
  }
}
