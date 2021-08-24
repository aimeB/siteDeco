import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeEvenementComponent } from '../list/type-evenement.component';
import { TypeEvenementDetailComponent } from '../detail/type-evenement-detail.component';
import { TypeEvenementUpdateComponent } from '../update/type-evenement-update.component';
import { TypeEvenementRoutingResolveService } from './type-evenement-routing-resolve.service';

const typeEvenementRoute: Routes = [
  {
    path: '',
    component: TypeEvenementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeEvenementDetailComponent,
    resolve: {
      typeEvenement: TypeEvenementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeEvenementUpdateComponent,
    resolve: {
      typeEvenement: TypeEvenementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeEvenementUpdateComponent,
    resolve: {
      typeEvenement: TypeEvenementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeEvenementRoute)],
  exports: [RouterModule],
})
export class TypeEvenementRoutingModule {}
