import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeEvenement, TypeEvenement } from '../type-evenement.model';
import { TypeEvenementService } from '../service/type-evenement.service';

@Injectable({ providedIn: 'root' })
export class TypeEvenementRoutingResolveService implements Resolve<ITypeEvenement> {
  constructor(protected service: TypeEvenementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeEvenement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeEvenement: HttpResponse<TypeEvenement>) => {
          if (typeEvenement.body) {
            return of(typeEvenement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeEvenement());
  }
}
