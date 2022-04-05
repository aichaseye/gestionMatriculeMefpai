import dayjs from 'dayjs/esm';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';

export interface ICarteScolaire {
  id?: number;
  longuer?: number;
  largeur?: number;
  dureeValidite?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  matriculeCart?: string | null;
  apprenant?: IApprenant | null;
}

export class CarteScolaire implements ICarteScolaire {
  constructor(
    public id?: number,
    public longuer?: number,
    public largeur?: number,
    public dureeValidite?: number | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateFin?: dayjs.Dayjs | null,
    public matriculeCart?: string | null,
    public apprenant?: IApprenant | null
  ) {}
}

export function getCarteScolaireIdentifier(carteScolaire: ICarteScolaire): number | undefined {
  return carteScolaire.id;
}
