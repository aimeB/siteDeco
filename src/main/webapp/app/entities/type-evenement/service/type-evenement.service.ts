import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeEvenement, getTypeEvenementIdentifier } from '../type-evenement.model';

export type EntityResponseType = HttpResponse<ITypeEvenement>;
export type EntityArrayResponseType = HttpResponse<ITypeEvenement[]>;

@Injectable({ providedIn: 'root' })
export class TypeEvenementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-evenements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeEvenement: ITypeEvenement): Observable<EntityResponseType> {
    return this.http.post<ITypeEvenement>(this.resourceUrl, typeEvenement, { observe: 'response' });
  }

  update(typeEvenement: ITypeEvenement): Observable<EntityResponseType> {
    return this.http.put<ITypeEvenement>(`${this.resourceUrl}/${getTypeEvenementIdentifier(typeEvenement) as number}`, typeEvenement, {
      observe: 'response',
    });
  }

  partialUpdate(typeEvenement: ITypeEvenement): Observable<EntityResponseType> {
    return this.http.patch<ITypeEvenement>(`${this.resourceUrl}/${getTypeEvenementIdentifier(typeEvenement) as number}`, typeEvenement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeEvenement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeEvenement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeEvenementToCollectionIfMissing(
    typeEvenementCollection: ITypeEvenement[],
    ...typeEvenementsToCheck: (ITypeEvenement | null | undefined)[]
  ): ITypeEvenement[] {
    const typeEvenements: ITypeEvenement[] = typeEvenementsToCheck.filter(isPresent);
    if (typeEvenements.length > 0) {
      const typeEvenementCollectionIdentifiers = typeEvenementCollection.map(
        typeEvenementItem => getTypeEvenementIdentifier(typeEvenementItem)!
      );
      const typeEvenementsToAdd = typeEvenements.filter(typeEvenementItem => {
        const typeEvenementIdentifier = getTypeEvenementIdentifier(typeEvenementItem);
        if (typeEvenementIdentifier == null || typeEvenementCollectionIdentifiers.includes(typeEvenementIdentifier)) {
          return false;
        }
        typeEvenementCollectionIdentifiers.push(typeEvenementIdentifier);
        return true;
      });
      return [...typeEvenementsToAdd, ...typeEvenementCollection];
    }
    return typeEvenementCollection;
  }
}
