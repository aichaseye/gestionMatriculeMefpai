import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDiplome, getDiplomeIdentifier } from '../diplome.model';

export type EntityResponseType = HttpResponse<IDiplome>;
export type EntityArrayResponseType = HttpResponse<IDiplome[]>;

@Injectable({ providedIn: 'root' })
export class DiplomeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/diplomes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(diplome: IDiplome): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(diplome);
    return this.http
      .post<IDiplome>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(diplome: IDiplome): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(diplome);
    return this.http
      .put<IDiplome>(`${this.resourceUrl}/${getDiplomeIdentifier(diplome) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(diplome: IDiplome): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(diplome);
    return this.http
      .patch<IDiplome>(`${this.resourceUrl}/${getDiplomeIdentifier(diplome) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDiplome>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDiplome[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDiplomeToCollectionIfMissing(diplomeCollection: IDiplome[], ...diplomesToCheck: (IDiplome | null | undefined)[]): IDiplome[] {
    const diplomes: IDiplome[] = diplomesToCheck.filter(isPresent);
    if (diplomes.length > 0) {
      const diplomeCollectionIdentifiers = diplomeCollection.map(diplomeItem => getDiplomeIdentifier(diplomeItem)!);
      const diplomesToAdd = diplomes.filter(diplomeItem => {
        const diplomeIdentifier = getDiplomeIdentifier(diplomeItem);
        if (diplomeIdentifier == null || diplomeCollectionIdentifiers.includes(diplomeIdentifier)) {
          return false;
        }
        diplomeCollectionIdentifiers.push(diplomeIdentifier);
        return true;
      });
      return [...diplomesToAdd, ...diplomeCollection];
    }
    return diplomeCollection;
  }

  protected convertDateFromClient(diplome: IDiplome): IDiplome {
    return Object.assign({}, diplome, {
      annee: diplome.annee?.isValid() ? diplome.annee.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((diplome: IDiplome) => {
        diplome.annee = diplome.annee ? dayjs(diplome.annee) : undefined;
      });
    }
    return res;
  }
}
