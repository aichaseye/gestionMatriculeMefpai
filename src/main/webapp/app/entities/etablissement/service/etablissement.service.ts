import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEtablissement, getEtablissementIdentifier } from '../etablissement.model';

export type EntityResponseType = HttpResponse<IEtablissement>;
export type EntityArrayResponseType = HttpResponse<IEtablissement[]>;

@Injectable({ providedIn: 'root' })
export class EtablissementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/etablissements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(etablissement: IEtablissement): Observable<EntityResponseType> {
    return this.http.post<IEtablissement>(this.resourceUrl, etablissement, { observe: 'response' });
  }

  update(etablissement: IEtablissement): Observable<EntityResponseType> {
    return this.http.put<IEtablissement>(`${this.resourceUrl}/${getEtablissementIdentifier(etablissement) as number}`, etablissement, {
      observe: 'response',
    });
  }

  partialUpdate(etablissement: IEtablissement): Observable<EntityResponseType> {
    return this.http.patch<IEtablissement>(`${this.resourceUrl}/${getEtablissementIdentifier(etablissement) as number}`, etablissement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEtablissement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtablissement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEtablissementToCollectionIfMissing(
    etablissementCollection: IEtablissement[],
    ...etablissementsToCheck: (IEtablissement | null | undefined)[]
  ): IEtablissement[] {
    const etablissements: IEtablissement[] = etablissementsToCheck.filter(isPresent);
    if (etablissements.length > 0) {
      const etablissementCollectionIdentifiers = etablissementCollection.map(
        etablissementItem => getEtablissementIdentifier(etablissementItem)!
      );
      const etablissementsToAdd = etablissements.filter(etablissementItem => {
        const etablissementIdentifier = getEtablissementIdentifier(etablissementItem);
        if (etablissementIdentifier == null || etablissementCollectionIdentifiers.includes(etablissementIdentifier)) {
          return false;
        }
        etablissementCollectionIdentifiers.push(etablissementIdentifier);
        return true;
      });
      return [...etablissementsToAdd, ...etablissementCollection];
    }
    return etablissementCollection;
  }
}
