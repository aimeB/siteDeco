import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'event',
        data: { pageTitle: 'decormoiApp.event.home.title' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'categorie-produit',
        data: { pageTitle: 'decormoiApp.categorieProduit.home.title' },
        loadChildren: () => import('./categorie-produit/categorie-produit.module').then(m => m.CategorieProduitModule),
      },
      {
        path: 'produit',
        data: { pageTitle: 'decormoiApp.produit.home.title' },
        loadChildren: () => import('./produit/produit.module').then(m => m.ProduitModule),
      },
      {
        path: 'type-evenement',
        data: { pageTitle: 'decormoiApp.typeEvenement.home.title' },
        loadChildren: () => import('./type-evenement/type-evenement.module').then(m => m.TypeEvenementModule),
      },
      {
        path: 'salle',
        data: { pageTitle: 'decormoiApp.salle.home.title' },
        loadChildren: () => import('./salle/salle.module').then(m => m.SalleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
