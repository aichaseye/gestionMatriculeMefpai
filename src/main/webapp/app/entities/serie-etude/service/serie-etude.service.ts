import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISerieEtude, getSerieEtudeIdentifier } from '../serie-etude.model';

export type EntityResponseType = HttpResponse<ISerieEtude>;
export type EntityArrayResponseType = HttpResponse<ISerieEtude[]>;

@Injectable({ providedIn: 'root' })
export class SerieEtudeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/serie-etudes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(serieEtude: ISerieEtude): Observable<EntityResponseType> {
    return this.http.post<ISerieEtude>(this.resourceUrl, serieEtude, { observe: 'response' });
  }

  update(serieEtude: ISerieEtude): Observable<EntityResponseType> {
    return this.http.put<ISerieEtude>(`${this.resourceUrl}/${getSerieEtudeIdentifier(serieEtude) as number}`, serieEtude, {
      observe: 'response',
    });
  }

  partialUpdate(serieEtude: ISerieEtude): Observable<EntityResponseType> {
    return this.http.patch<ISerieEtude>(`${this.resourceUrl}/${getSerieEtudeIdentifier(serieEtude) as number}`, serieEtude, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISerieEtude>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISerieEtude[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSerieEtudeToCollectionIfMissing(
    serieEtudeCollection: ISerieEtude[],
    ...serieEtudesToCheck: (ISerieEtude | null | undefined)[]
  ): ISerieEtude[] {
    const serieEtudes: ISerieEtude[] = serieEtudesToCheck.filter(isPresent);
    if (serieEtudes.length > 0) {
      const serieEtudeCollectionIdentifiers = serieEtudeCollection.map(serieEtudeItem => getSerieEtudeIdentifier(serieEtudeItem)!);
      const serieEtudesToAdd = serieEtudes.filter(serieEtudeItem => {
        const serieEtudeIdentifier = getSerieEtudeIdentifier(serieEtudeItem);
        if (serieEtudeIdentifier == null || serieEtudeCollectionIdentifiers.includes(serieEtudeIdentifier)) {
          return false;
        }
        serieEtudeCollectionIdentifiers.push(serieEtudeIdentifier);
        return true;
      });
      return [...serieEtudesToAdd, ...serieEtudeCollection];
    }
    return serieEtudeCollection;
  }
}
