import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemandeMatEtab, getDemandeMatEtabIdentifier } from '../demande-mat-etab.model';

export type EntityResponseType = HttpResponse<IDemandeMatEtab>;
export type EntityArrayResponseType = HttpResponse<IDemandeMatEtab[]>;

@Injectable({ providedIn: 'root' })
export class DemandeMatEtabService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demande-mat-etabs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demandeMatEtab: IDemandeMatEtab): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeMatEtab);
    return this.http
      .post<IDemandeMatEtab>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(demandeMatEtab: IDemandeMatEtab): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeMatEtab);
    return this.http
      .put<IDemandeMatEtab>(`${this.resourceUrl}/${getDemandeMatEtabIdentifier(demandeMatEtab) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(demandeMatEtab: IDemandeMatEtab): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeMatEtab);
    return this.http
      .patch<IDemandeMatEtab>(`${this.resourceUrl}/${getDemandeMatEtabIdentifier(demandeMatEtab) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDemandeMatEtab>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDemandeMatEtab[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeMatEtabToCollectionIfMissing(
    demandeMatEtabCollection: IDemandeMatEtab[],
    ...demandeMatEtabsToCheck: (IDemandeMatEtab | null | undefined)[]
  ): IDemandeMatEtab[] {
    const demandeMatEtabs: IDemandeMatEtab[] = demandeMatEtabsToCheck.filter(isPresent);
    if (demandeMatEtabs.length > 0) {
      const demandeMatEtabCollectionIdentifiers = demandeMatEtabCollection.map(
        demandeMatEtabItem => getDemandeMatEtabIdentifier(demandeMatEtabItem)!
      );
      const demandeMatEtabsToAdd = demandeMatEtabs.filter(demandeMatEtabItem => {
        const demandeMatEtabIdentifier = getDemandeMatEtabIdentifier(demandeMatEtabItem);
        if (demandeMatEtabIdentifier == null || demandeMatEtabCollectionIdentifiers.includes(demandeMatEtabIdentifier)) {
          return false;
        }
        demandeMatEtabCollectionIdentifiers.push(demandeMatEtabIdentifier);
        return true;
      });
      return [...demandeMatEtabsToAdd, ...demandeMatEtabCollection];
    }
    return demandeMatEtabCollection;
  }

  protected convertDateFromClient(demandeMatEtab: IDemandeMatEtab): IDemandeMatEtab {
    return Object.assign({}, demandeMatEtab, {
      dateDemande: demandeMatEtab.dateDemande?.isValid() ? demandeMatEtab.dateDemande.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDemande = res.body.dateDemande ? dayjs(res.body.dateDemande) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((demandeMatEtab: IDemandeMatEtab) => {
        demandeMatEtab.dateDemande = demandeMatEtab.dateDemande ? dayjs(demandeMatEtab.dateDemande) : undefined;
      });
    }
    return res;
  }
}
