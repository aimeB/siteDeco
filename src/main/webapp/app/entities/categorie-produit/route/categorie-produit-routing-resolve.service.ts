import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategorieProduit, CategorieProduit } from '../categorie-produit.model';
import { CategorieProduitService } from '../service/categorie-produit.service';

@Injectable({ providedIn: 'root' })
export class CategorieProduitRoutingResolveService implements Resolve<ICategorieProduit> {
  constructor(protected service: CategorieProduitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategorieProduit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categorieProduit: HttpResponse<CategorieProduit>) => {
          if (categorieProduit.body) {
            return of(categorieProduit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CategorieProduit());
  }
}
