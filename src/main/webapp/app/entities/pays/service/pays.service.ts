import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPays, getPaysIdentifier } from '../pays.model';

export type EntityResponseType = HttpResponse<IPays>;
export type EntityArrayResponseType = HttpResponse<IPays[]>;

@Injectable({ providedIn: 'root' })
export class PaysService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pays: IPays): Observable<EntityResponseType> {
    return this.http.post<IPays>(this.resourceUrl, pays, { observe: 'response' });
  }

  update(pays: IPays): Observable<EntityResponseType> {
    return this.http.put<IPays>(`${this.resourceUrl}/${getPaysIdentifier(pays) as number}`, pays, { observe: 'response' });
  }

  partialUpdate(pays: IPays): Observable<EntityResponseType> {
    return this.http.patch<IPays>(`${this.resourceUrl}/${getPaysIdentifier(pays) as number}`, pays, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPays>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPays[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPaysToCollectionIfMissing(paysCollection: IPays[], ...paysToCheck: (IPays | null | undefined)[]): IPays[] {
    const pays: IPays[] = paysToCheck.filter(isPresent);
    if (pays.length > 0) {
      const paysCollectionIdentifiers = paysCollection.map(paysItem => getPaysIdentifier(paysItem)!);
      const paysToAdd = pays.filter(paysItem => {
        const paysIdentifier = getPaysIdentifier(paysItem);
        if (paysIdentifier == null || paysCollectionIdentifiers.includes(paysIdentifier)) {
          return false;
        }
        paysCollectionIdentifiers.push(paysIdentifier);
        return true;
      });
      return [...paysToAdd, ...paysCollection];
    }
    return paysCollection;
  }
}
