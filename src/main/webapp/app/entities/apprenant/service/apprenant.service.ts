import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApprenant, getApprenantIdentifier } from '../apprenant.model';

export type EntityResponseType = HttpResponse<IApprenant>;
export type EntityArrayResponseType = HttpResponse<IApprenant[]>;

@Injectable({ providedIn: 'root' })
export class ApprenantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apprenants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(apprenant: IApprenant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apprenant);
    return this.http
      .post<IApprenant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(apprenant: IApprenant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apprenant);
    return this.http
      .put<IApprenant>(`${this.resourceUrl}/${getApprenantIdentifier(apprenant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(apprenant: IApprenant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(apprenant);
    return this.http
      .patch<IApprenant>(`${this.resourceUrl}/${getApprenantIdentifier(apprenant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApprenant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApprenant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApprenantToCollectionIfMissing(
    apprenantCollection: IApprenant[],
    ...apprenantsToCheck: (IApprenant | null | undefined)[]
  ): IApprenant[] {
    const apprenants: IApprenant[] = apprenantsToCheck.filter(isPresent);
    if (apprenants.length > 0) {
      const apprenantCollectionIdentifiers = apprenantCollection.map(apprenantItem => getApprenantIdentifier(apprenantItem)!);
      const apprenantsToAdd = apprenants.filter(apprenantItem => {
        const apprenantIdentifier = getApprenantIdentifier(apprenantItem);
        if (apprenantIdentifier == null || apprenantCollectionIdentifiers.includes(apprenantIdentifier)) {
          return false;
        }
        apprenantCollectionIdentifiers.push(apprenantIdentifier);
        return true;
      });
      return [...apprenantsToAdd, ...apprenantCollection];
    }
    return apprenantCollection;
  }

  protected convertDateFromClient(apprenant: IApprenant): IApprenant {
    return Object.assign({}, apprenant, {
      dateNaissance: apprenant.dateNaissance?.isValid() ? apprenant.dateNaissance.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateNaissance = res.body.dateNaissance ? dayjs(res.body.dateNaissance) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((apprenant: IApprenant) => {
        apprenant.dateNaissance = apprenant.dateNaissance ? dayjs(apprenant.dateNaissance) : undefined;
      });
    }
    return res;
  }
}
