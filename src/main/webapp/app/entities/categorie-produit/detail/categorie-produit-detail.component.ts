import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategorieProduit } from '../categorie-produit.model';

@Component({
  selector: 'jhi-categorie-produit-detail',
  templateUrl: './categorie-produit-detail.component.html',
})
export class CategorieProduitDetailComponent implements OnInit {
  categorieProduit: ICategorieProduit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categorieProduit }) => {
      this.categorieProduit = categorieProduit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
