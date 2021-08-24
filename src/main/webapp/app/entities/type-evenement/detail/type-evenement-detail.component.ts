import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeEvenement } from '../type-evenement.model';

@Component({
  selector: 'jhi-type-evenement-detail',
  templateUrl: './type-evenement-detail.component.html',
})
export class TypeEvenementDetailComponent implements OnInit {
  typeEvenement: ITypeEvenement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeEvenement }) => {
      this.typeEvenement = typeEvenement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
