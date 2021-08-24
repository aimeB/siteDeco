export interface ISalle {
  id?: number;
  nom?: string;
}

export class Salle implements ISalle {
  constructor(public id?: number, public nom?: string) {}
}

export function getSalleIdentifier(salle: ISalle): number | undefined {
  return salle.id;
}
