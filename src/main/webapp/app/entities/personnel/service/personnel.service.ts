import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonnel, getPersonnelIdentifier } from '../personnel.model';

export type EntityResponseType = HttpResponse<IPersonnel>;
export type EntityArrayResponseType = HttpResponse<IPersonnel[]>;

@Injectable({ providedIn: 'root' })
export class PersonnelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personnel');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personnel: IPersonnel): Observable<EntityResponseType> {
    return this.http.post<IPersonnel>(this.resourceUrl, personnel, { observe: 'response' });
  }

  update(personnel: IPersonnel): Observable<EntityResponseType> {
    return this.http.put<IPersonnel>(`${this.resourceUrl}/${getPersonnelIdentifier(personnel) as number}`, personnel, {
      observe: 'response',
    });
  }

  partialUpdate(personnel: IPersonnel): Observable<EntityResponseType> {
    return this.http.patch<IPersonnel>(`${this.resourceUrl}/${getPersonnelIdentifier(personnel) as number}`, personnel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonnel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonnel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonnelToCollectionIfMissing(
    personnelCollection: IPersonnel[],
    ...personnelToCheck: (IPersonnel | null | undefined)[]
  ): IPersonnel[] {
    const personnel: IPersonnel[] = personnelToCheck.filter(isPresent);
    if (personnel.length > 0) {
      const personnelCollectionIdentifiers = personnelCollection.map(personnelItem => getPersonnelIdentifier(personnelItem)!);
      const personnelToAdd = personnel.filter(personnelItem => {
        const personnelIdentifier = getPersonnelIdentifier(personnelItem);
        if (personnelIdentifier == null || personnelCollectionIdentifiers.includes(personnelIdentifier)) {
          return false;
        }
        personnelCollectionIdentifiers.push(personnelIdentifier);
        return true;
      });
      return [...personnelToAdd, ...personnelCollection];
    }
    return personnelCollection;
  }
}
