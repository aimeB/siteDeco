export interface ITypeEvenement {
  id?: number;
  nom?: string;
}

export class TypeEvenement implements ITypeEvenement {
  constructor(public id?: number, public nom?: string) {}
}

export function getTypeEvenementIdentifier(typeEvenement: ITypeEvenement): number | undefined {
  return typeEvenement.id;
}
