import { ICategorieProduit } from 'app/entities/categorie-produit/categorie-produit.model';

export interface IProduit {
  id?: number;
  nom?: string;
  description?: string;
  prix?: number;
  imageContentType?: string;
  image?: string;
  categorie?: ICategorieProduit;
}

export class Produit implements IProduit {
  constructor(
    public id?: number,
    public nom?: string,
    public description?: string,
    public prix?: number,
    public imageContentType?: string,
    public image?: string,
    public categorie?: ICategorieProduit
  ) {}
}

export function getProduitIdentifier(produit: IProduit): number | undefined {
  return produit.id;
}
