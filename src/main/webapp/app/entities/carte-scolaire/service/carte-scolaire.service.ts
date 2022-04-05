import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICarteScolaire, getCarteScolaireIdentifier } from '../carte-scolaire.model';

export type EntityResponseType = HttpResponse<ICarteScolaire>;
export type EntityArrayResponseType = HttpResponse<ICarteScolaire[]>;

@Injectable({ providedIn: 'root' })
export class CarteScolaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/carte-scolaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(carteScolaire: ICarteScolaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(carteScolaire);
    return this.http
      .post<ICarteScolaire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(carteScolaire: ICarteScolaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(carteScolaire);
    return this.http
      .put<ICarteScolaire>(`${this.resourceUrl}/${getCarteScolaireIdentifier(carteScolaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(carteScolaire: ICarteScolaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(carteScolaire);
    return this.http
      .patch<ICarteScolaire>(`${this.resourceUrl}/${getCarteScolaireIdentifier(carteScolaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICarteScolaire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICarteScolaire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCarteScolaireToCollectionIfMissing(
    carteScolaireCollection: ICarteScolaire[],
    ...carteScolairesToCheck: (ICarteScolaire | null | undefined)[]
  ): ICarteScolaire[] {
    const carteScolaires: ICarteScolaire[] = carteScolairesToCheck.filter(isPresent);
    if (carteScolaires.length > 0) {
      const carteScolaireCollectionIdentifiers = carteScolaireCollection.map(
        carteScolaireItem => getCarteScolaireIdentifier(carteScolaireItem)!
      );
      const carteScolairesToAdd = carteScolaires.filter(carteScolaireItem => {
        const carteScolaireIdentifier = getCarteScolaireIdentifier(carteScolaireItem);
        if (carteScolaireIdentifier == null || carteScolaireCollectionIdentifiers.includes(carteScolaireIdentifier)) {
          return false;
        }
        carteScolaireCollectionIdentifiers.push(carteScolaireIdentifier);
        return true;
      });
      return [...carteScolairesToAdd, ...carteScolaireCollection];
    }
    return carteScolaireCollection;
  }

  protected convertDateFromClient(carteScolaire: ICarteScolaire): ICarteScolaire {
    return Object.assign({}, carteScolaire, {
      dateCreation: carteScolaire.dateCreation?.isValid() ? carteScolaire.dateCreation.format(DATE_FORMAT) : undefined,
      dateFin: carteScolaire.dateFin?.isValid() ? carteScolaire.dateFin.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
      res.body.dateFin = res.body.dateFin ? dayjs(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((carteScolaire: ICarteScolaire) => {
        carteScolaire.dateCreation = carteScolaire.dateCreation ? dayjs(carteScolaire.dateCreation) : undefined;
        carteScolaire.dateFin = carteScolaire.dateFin ? dayjs(carteScolaire.dateFin) : undefined;
      });
    }
    return res;
  }
}
