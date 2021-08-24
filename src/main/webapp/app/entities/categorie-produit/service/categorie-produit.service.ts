import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategorieProduit, getCategorieProduitIdentifier } from '../categorie-produit.model';

export type EntityResponseType = HttpResponse<ICategorieProduit>;
export type EntityArrayResponseType = HttpResponse<ICategorieProduit[]>;

@Injectable({ providedIn: 'root' })
export class CategorieProduitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categorie-produits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categorieProduit: ICategorieProduit): Observable<EntityResponseType> {
    return this.http.post<ICategorieProduit>(this.resourceUrl, categorieProduit, { observe: 'response' });
  }

  update(categorieProduit: ICategorieProduit): Observable<EntityResponseType> {
    return this.http.put<ICategorieProduit>(
      `${this.resourceUrl}/${getCategorieProduitIdentifier(categorieProduit) as number}`,
      categorieProduit,
      { observe: 'response' }
    );
  }

  partialUpdate(categorieProduit: ICategorieProduit): Observable<EntityResponseType> {
    return this.http.patch<ICategorieProduit>(
      `${this.resourceUrl}/${getCategorieProduitIdentifier(categorieProduit) as number}`,
      categorieProduit,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategorieProduit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategorieProduit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCategorieProduitToCollectionIfMissing(
    categorieProduitCollection: ICategorieProduit[],
    ...categorieProduitsToCheck: (ICategorieProduit | null | undefined)[]
  ): ICategorieProduit[] {
    const categorieProduits: ICategorieProduit[] = categorieProduitsToCheck.filter(isPresent);
    if (categorieProduits.length > 0) {
      const categorieProduitCollectionIdentifiers = categorieProduitCollection.map(
        categorieProduitItem => getCategorieProduitIdentifier(categorieProduitItem)!
      );
      const categorieProduitsToAdd = categorieProduits.filter(categorieProduitItem => {
        const categorieProduitIdentifier = getCategorieProduitIdentifier(categorieProduitItem);
        if (categorieProduitIdentifier == null || categorieProduitCollectionIdentifiers.includes(categorieProduitIdentifier)) {
          return false;
        }
        categorieProduitCollectionIdentifiers.push(categorieProduitIdentifier);
        return true;
      });
      return [...categorieProduitsToAdd, ...categorieProduitCollection];
    }
    return categorieProduitCollection;
  }
}
