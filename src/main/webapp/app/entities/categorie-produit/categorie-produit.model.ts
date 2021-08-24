export interface ICategorieProduit {
  id?: number;
  nom?: string;
}

export class CategorieProduit implements ICategorieProduit {
  constructor(public id?: number, public nom?: string) {}
}

export function getCategorieProduitIdentifier(categorieProduit: ICategorieProduit): number | undefined {
  return categorieProduit.id;
}
