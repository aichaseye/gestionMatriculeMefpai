import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMatiere, getMatiereIdentifier } from '../matiere.model';

export type EntityResponseType = HttpResponse<IMatiere>;
export type EntityArrayResponseType = HttpResponse<IMatiere[]>;

@Injectable({ providedIn: 'root' })
export class MatiereService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/matieres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(matiere: IMatiere): Observable<EntityResponseType> {
    return this.http.post<IMatiere>(this.resourceUrl, matiere, { observe: 'response' });
  }

  update(matiere: IMatiere): Observable<EntityResponseType> {
    return this.http.put<IMatiere>(`${this.resourceUrl}/${getMatiereIdentifier(matiere) as number}`, matiere, { observe: 'response' });
  }

  partialUpdate(matiere: IMatiere): Observable<EntityResponseType> {
    return this.http.patch<IMatiere>(`${this.resourceUrl}/${getMatiereIdentifier(matiere) as number}`, matiere, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMatiere>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMatiere[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMatiereToCollectionIfMissing(matiereCollection: IMatiere[], ...matieresToCheck: (IMatiere | null | undefined)[]): IMatiere[] {
    const matieres: IMatiere[] = matieresToCheck.filter(isPresent);
    if (matieres.length > 0) {
      const matiereCollectionIdentifiers = matiereCollection.map(matiereItem => getMatiereIdentifier(matiereItem)!);
      const matieresToAdd = matieres.filter(matiereItem => {
        const matiereIdentifier = getMatiereIdentifier(matiereItem);
        if (matiereIdentifier == null || matiereCollectionIdentifiers.includes(matiereIdentifier)) {
          return false;
        }
        matiereCollectionIdentifiers.push(matiereIdentifier);
        return true;
      });
      return [...matieresToAdd, ...matiereCollection];
    }
    return matiereCollection;
  }
}
