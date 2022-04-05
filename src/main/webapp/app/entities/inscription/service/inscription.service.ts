import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInscription, getInscriptionIdentifier } from '../inscription.model';

export type EntityResponseType = HttpResponse<IInscription>;
export type EntityArrayResponseType = HttpResponse<IInscription[]>;

@Injectable({ providedIn: 'root' })
export class InscriptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inscriptions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inscription: IInscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscription);
    return this.http
      .post<IInscription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(inscription: IInscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscription);
    return this.http
      .put<IInscription>(`${this.resourceUrl}/${getInscriptionIdentifier(inscription) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(inscription: IInscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscription);
    return this.http
      .patch<IInscription>(`${this.resourceUrl}/${getInscriptionIdentifier(inscription) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInscription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInscriptionToCollectionIfMissing(
    inscriptionCollection: IInscription[],
    ...inscriptionsToCheck: (IInscription | null | undefined)[]
  ): IInscription[] {
    const inscriptions: IInscription[] = inscriptionsToCheck.filter(isPresent);
    if (inscriptions.length > 0) {
      const inscriptionCollectionIdentifiers = inscriptionCollection.map(inscriptionItem => getInscriptionIdentifier(inscriptionItem)!);
      const inscriptionsToAdd = inscriptions.filter(inscriptionItem => {
        const inscriptionIdentifier = getInscriptionIdentifier(inscriptionItem);
        if (inscriptionIdentifier == null || inscriptionCollectionIdentifiers.includes(inscriptionIdentifier)) {
          return false;
        }
        inscriptionCollectionIdentifiers.push(inscriptionIdentifier);
        return true;
      });
      return [...inscriptionsToAdd, ...inscriptionCollection];
    }
    return inscriptionCollection;
  }

  protected convertDateFromClient(inscription: IInscription): IInscription {
    return Object.assign({}, inscription, {
      anneeIns: inscription.anneeIns?.isValid() ? inscription.anneeIns.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.anneeIns = res.body.anneeIns ? dayjs(res.body.anneeIns) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((inscription: IInscription) => {
        inscription.anneeIns = inscription.anneeIns ? dayjs(inscription.anneeIns) : undefined;
      });
    }
    return res;
  }
}
