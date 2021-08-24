import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategorieProduitComponent } from '../list/categorie-produit.component';
import { CategorieProduitDetailComponent } from '../detail/categorie-produit-detail.component';
import { CategorieProduitUpdateComponent } from '../update/categorie-produit-update.component';
import { CategorieProduitRoutingResolveService } from './categorie-produit-routing-resolve.service';

const categorieProduitRoute: Routes = [
  {
    path: '',
    component: CategorieProduitComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategorieProduitDetailComponent,
    resolve: {
      categorieProduit: CategorieProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategorieProduitUpdateComponent,
    resolve: {
      categorieProduit: CategorieProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategorieProduitUpdateComponent,
    resolve: {
      categorieProduit: CategorieProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categorieProduitRoute)],
  exports: [RouterModule],
})
export class CategorieProduitRoutingModule {}
