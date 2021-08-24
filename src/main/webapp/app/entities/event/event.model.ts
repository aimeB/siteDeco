import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ITypeEvenement } from 'app/entities/type-evenement/type-evenement.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { ISalle } from 'app/entities/salle/salle.model';

export interface IEvent {
  id?: number;
  nom?: string;
  dateEvenement?: dayjs.Dayjs;
  prix?: number | null;
  appartenantA?: IUser | null;
  agentEvenements?: IUser[] | null;
  typeEvenement?: ITypeEvenement;
  produits?: IProduit[] | null;
  salle?: ISalle;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public nom?: string,
    public dateEvenement?: dayjs.Dayjs,
    public prix?: number | null,
    public appartenantA?: IUser | null,
    public agentEvenements?: IUser[] | null,
    public typeEvenement?: ITypeEvenement,
    public produits?: IProduit[] | null,
    public salle?: ISalle
  ) {}
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
