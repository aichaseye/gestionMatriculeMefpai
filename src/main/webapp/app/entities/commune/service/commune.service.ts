import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommune, getCommuneIdentifier } from '../commune.model';

export type EntityResponseType = HttpResponse<ICommune>;
export type EntityArrayResponseType = HttpResponse<ICommune[]>;

@Injectable({ providedIn: 'root' })
export class CommuneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/communes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commune: ICommune): Observable<EntityResponseType> {
    return this.http.post<ICommune>(this.resourceUrl, commune, { observe: 'response' });
  }

  update(commune: ICommune): Observable<EntityResponseType> {
    return this.http.put<ICommune>(`${this.resourceUrl}/${getCommuneIdentifier(commune) as number}`, commune, { observe: 'response' });
  }

  partialUpdate(commune: ICommune): Observable<EntityResponseType> {
    return this.http.patch<ICommune>(`${this.resourceUrl}/${getCommuneIdentifier(commune) as number}`, commune, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommune>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommune[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommuneToCollectionIfMissing(communeCollection: ICommune[], ...communesToCheck: (ICommune | null | undefined)[]): ICommune[] {
    const communes: ICommune[] = communesToCheck.filter(isPresent);
    if (communes.length > 0) {
      const communeCollectionIdentifiers = communeCollection.map(communeItem => getCommuneIdentifier(communeItem)!);
      const communesToAdd = communes.filter(communeItem => {
        const communeIdentifier = getCommuneIdentifier(communeItem);
        if (communeIdentifier == null || communeCollectionIdentifiers.includes(communeIdentifier)) {
          return false;
        }
        communeCollectionIdentifiers.push(communeIdentifier);
        return true;
      });
      return [...communesToAdd, ...communeCollection];
    }
    return communeCollection;
  }
}
