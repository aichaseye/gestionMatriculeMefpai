import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDepartement, getDepartementIdentifier } from '../departement.model';

export type EntityResponseType = HttpResponse<IDepartement>;
export type EntityArrayResponseType = HttpResponse<IDepartement[]>;

@Injectable({ providedIn: 'root' })
export class DepartementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/departements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(departement: IDepartement): Observable<EntityResponseType> {
    return this.http.post<IDepartement>(this.resourceUrl, departement, { observe: 'response' });
  }

  update(departement: IDepartement): Observable<EntityResponseType> {
    return this.http.put<IDepartement>(`${this.resourceUrl}/${getDepartementIdentifier(departement) as number}`, departement, {
      observe: 'response',
    });
  }

  partialUpdate(departement: IDepartement): Observable<EntityResponseType> {
    return this.http.patch<IDepartement>(`${this.resourceUrl}/${getDepartementIdentifier(departement) as number}`, departement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDepartement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDepartement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDepartementToCollectionIfMissing(
    departementCollection: IDepartement[],
    ...departementsToCheck: (IDepartement | null | undefined)[]
  ): IDepartement[] {
    const departements: IDepartement[] = departementsToCheck.filter(isPresent);
    if (departements.length > 0) {
      const departementCollectionIdentifiers = departementCollection.map(departementItem => getDepartementIdentifier(departementItem)!);
      const departementsToAdd = departements.filter(departementItem => {
        const departementIdentifier = getDepartementIdentifier(departementItem);
        if (departementIdentifier == null || departementCollectionIdentifiers.includes(departementIdentifier)) {
          return false;
        }
        departementCollectionIdentifiers.push(departementIdentifier);
        return true;
      });
      return [...departementsToAdd, ...departementCollection];
    }
    return departementCollection;
  }
}
