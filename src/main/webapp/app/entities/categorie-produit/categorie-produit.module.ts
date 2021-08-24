import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CategorieProduitComponent } from './list/categorie-produit.component';
import { CategorieProduitDetailComponent } from './detail/categorie-produit-detail.component';
import { CategorieProduitUpdateComponent } from './update/categorie-produit-update.component';
import { CategorieProduitDeleteDialogComponent } from './delete/categorie-produit-delete-dialog.component';
import { CategorieProduitRoutingModule } from './route/categorie-produit-routing.module';

@NgModule({
  imports: [SharedModule, CategorieProduitRoutingModule],
  declarations: [
    CategorieProduitComponent,
    CategorieProduitDetailComponent,
    CategorieProduitUpdateComponent,
    CategorieProduitDeleteDialogComponent,
  ],
  entryComponents: [CategorieProduitDeleteDialogComponent],
})
export class CategorieProduitModule {}
