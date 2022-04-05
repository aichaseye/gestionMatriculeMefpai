import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INiveauEtude, getNiveauEtudeIdentifier } from '../niveau-etude.model';

export type EntityResponseType = HttpResponse<INiveauEtude>;
export type EntityArrayResponseType = HttpResponse<INiveauEtude[]>;

@Injectable({ providedIn: 'root' })
export class NiveauEtudeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/niveau-etudes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(niveauEtude: INiveauEtude): Observable<EntityResponseType> {
    return this.http.post<INiveauEtude>(this.resourceUrl, niveauEtude, { observe: 'response' });
  }

  update(niveauEtude: INiveauEtude): Observable<EntityResponseType> {
    return this.http.put<INiveauEtude>(`${this.resourceUrl}/${getNiveauEtudeIdentifier(niveauEtude) as number}`, niveauEtude, {
      observe: 'response',
    });
  }

  partialUpdate(niveauEtude: INiveauEtude): Observable<EntityResponseType> {
    return this.http.patch<INiveauEtude>(`${this.resourceUrl}/${getNiveauEtudeIdentifier(niveauEtude) as number}`, niveauEtude, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INiveauEtude>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INiveauEtude[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNiveauEtudeToCollectionIfMissing(
    niveauEtudeCollection: INiveauEtude[],
    ...niveauEtudesToCheck: (INiveauEtude | null | undefined)[]
  ): INiveauEtude[] {
    const niveauEtudes: INiveauEtude[] = niveauEtudesToCheck.filter(isPresent);
    if (niveauEtudes.length > 0) {
      const niveauEtudeCollectionIdentifiers = niveauEtudeCollection.map(niveauEtudeItem => getNiveauEtudeIdentifier(niveauEtudeItem)!);
      const niveauEtudesToAdd = niveauEtudes.filter(niveauEtudeItem => {
        const niveauEtudeIdentifier = getNiveauEtudeIdentifier(niveauEtudeItem);
        if (niveauEtudeIdentifier == null || niveauEtudeCollectionIdentifiers.includes(niveauEtudeIdentifier)) {
          return false;
        }
        niveauEtudeCollectionIdentifiers.push(niveauEtudeIdentifier);
        return true;
      });
      return [...niveauEtudesToAdd, ...niveauEtudeCollection];
    }
    return niveauEtudeCollection;
  }
}
