import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategorieProduit } from '../categorie-produit.model';
import { CategorieProduitService } from '../service/categorie-produit.service';

@Component({
  templateUrl: './categorie-produit-delete-dialog.component.html',
})
export class CategorieProduitDeleteDialogComponent {
  categorieProduit?: ICategorieProduit;

  constructor(protected categorieProduitService: CategorieProduitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categorieProduitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
