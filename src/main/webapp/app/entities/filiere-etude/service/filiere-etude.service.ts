import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFiliereEtude, getFiliereEtudeIdentifier } from '../filiere-etude.model';

export type EntityResponseType = HttpResponse<IFiliereEtude>;
export type EntityArrayResponseType = HttpResponse<IFiliereEtude[]>;

@Injectable({ providedIn: 'root' })
export class FiliereEtudeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/filiere-etudes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(filiereEtude: IFiliereEtude): Observable<EntityResponseType> {
    return this.http.post<IFiliereEtude>(this.resourceUrl, filiereEtude, { observe: 'response' });
  }

  update(filiereEtude: IFiliereEtude): Observable<EntityResponseType> {
    return this.http.put<IFiliereEtude>(`${this.resourceUrl}/${getFiliereEtudeIdentifier(filiereEtude) as number}`, filiereEtude, {
      observe: 'response',
    });
  }

  partialUpdate(filiereEtude: IFiliereEtude): Observable<EntityResponseType> {
    return this.http.patch<IFiliereEtude>(`${this.resourceUrl}/${getFiliereEtudeIdentifier(filiereEtude) as number}`, filiereEtude, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFiliereEtude>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFiliereEtude[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFiliereEtudeToCollectionIfMissing(
    filiereEtudeCollection: IFiliereEtude[],
    ...filiereEtudesToCheck: (IFiliereEtude | null | undefined)[]
  ): IFiliereEtude[] {
    const filiereEtudes: IFiliereEtude[] = filiereEtudesToCheck.filter(isPresent);
    if (filiereEtudes.length > 0) {
      const filiereEtudeCollectionIdentifiers = filiereEtudeCollection.map(
        filiereEtudeItem => getFiliereEtudeIdentifier(filiereEtudeItem)!
      );
      const filiereEtudesToAdd = filiereEtudes.filter(filiereEtudeItem => {
        const filiereEtudeIdentifier = getFiliereEtudeIdentifier(filiereEtudeItem);
        if (filiereEtudeIdentifier == null || filiereEtudeCollectionIdentifiers.includes(filiereEtudeIdentifier)) {
          return false;
        }
        filiereEtudeCollectionIdentifiers.push(filiereEtudeIdentifier);
        return true;
      });
      return [...filiereEtudesToAdd, ...filiereEtudeCollection];
    }
    return filiereEtudeCollection;
  }
}
