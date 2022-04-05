import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemandeMatApp, getDemandeMatAppIdentifier } from '../demande-mat-app.model';

export type EntityResponseType = HttpResponse<IDemandeMatApp>;
export type EntityArrayResponseType = HttpResponse<IDemandeMatApp[]>;

@Injectable({ providedIn: 'root' })
export class DemandeMatAppService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demande-mat-apps');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demandeMatApp: IDemandeMatApp): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeMatApp);
    return this.http
      .post<IDemandeMatApp>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(demandeMatApp: IDemandeMatApp): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeMatApp);
    return this.http
      .put<IDemandeMatApp>(`${this.resourceUrl}/${getDemandeMatAppIdentifier(demandeMatApp) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(demandeMatApp: IDemandeMatApp): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demandeMatApp);
    return this.http
      .patch<IDemandeMatApp>(`${this.resourceUrl}/${getDemandeMatAppIdentifier(demandeMatApp) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDemandeMatApp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDemandeMatApp[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeMatAppToCollectionIfMissing(
    demandeMatAppCollection: IDemandeMatApp[],
    ...demandeMatAppsToCheck: (IDemandeMatApp | null | undefined)[]
  ): IDemandeMatApp[] {
    const demandeMatApps: IDemandeMatApp[] = demandeMatAppsToCheck.filter(isPresent);
    if (demandeMatApps.length > 0) {
      const demandeMatAppCollectionIdentifiers = demandeMatAppCollection.map(
        demandeMatAppItem => getDemandeMatAppIdentifier(demandeMatAppItem)!
      );
      const demandeMatAppsToAdd = demandeMatApps.filter(demandeMatAppItem => {
        const demandeMatAppIdentifier = getDemandeMatAppIdentifier(demandeMatAppItem);
        if (demandeMatAppIdentifier == null || demandeMatAppCollectionIdentifiers.includes(demandeMatAppIdentifier)) {
          return false;
        }
        demandeMatAppCollectionIdentifiers.push(demandeMatAppIdentifier);
        return true;
      });
      return [...demandeMatAppsToAdd, ...demandeMatAppCollection];
    }
    return demandeMatAppCollection;
  }

  protected convertDateFromClient(demandeMatApp: IDemandeMatApp): IDemandeMatApp {
    return Object.assign({}, demandeMatApp, {
      dateDemande: demandeMatApp.dateDemande?.isValid() ? demandeMatApp.dateDemande.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((demandeMatApp: IDemandeMatApp) => {
        demandeMatApp.dateDemande = demandeMatApp.dateDemande ? dayjs(demandeMatApp.dateDemande) : undefined;
      });
    }
    return res;
  }
}
