import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInspection, getInspectionIdentifier } from '../inspection.model';

export type EntityResponseType = HttpResponse<IInspection>;
export type EntityArrayResponseType = HttpResponse<IInspection[]>;

@Injectable({ providedIn: 'root' })
export class InspectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inspections');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inspection: IInspection): Observable<EntityResponseType> {
    return this.http.post<IInspection>(this.resourceUrl, inspection, { observe: 'response' });
  }

  update(inspection: IInspection): Observable<EntityResponseType> {
    return this.http.put<IInspection>(`${this.resourceUrl}/${getInspectionIdentifier(inspection) as number}`, inspection, {
      observe: 'response',
    });
  }

  partialUpdate(inspection: IInspection): Observable<EntityResponseType> {
    return this.http.patch<IInspection>(`${this.resourceUrl}/${getInspectionIdentifier(inspection) as number}`, inspection, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInspection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInspection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInspectionToCollectionIfMissing(
    inspectionCollection: IInspection[],
    ...inspectionsToCheck: (IInspection | null | undefined)[]
  ): IInspection[] {
    const inspections: IInspection[] = inspectionsToCheck.filter(isPresent);
    if (inspections.length > 0) {
      const inspectionCollectionIdentifiers = inspectionCollection.map(inspectionItem => getInspectionIdentifier(inspectionItem)!);
      const inspectionsToAdd = inspections.filter(inspectionItem => {
        const inspectionIdentifier = getInspectionIdentifier(inspectionItem);
        if (inspectionIdentifier == null || inspectionCollectionIdentifiers.includes(inspectionIdentifier)) {
          return false;
        }
        inspectionCollectionIdentifiers.push(inspectionIdentifier);
        return true;
      });
      return [...inspectionsToAdd, ...inspectionCollection];
    }
    return inspectionCollection;
  }
}
