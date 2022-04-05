import dayjs from 'dayjs/esm';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';

export interface IBon {
  id?: number;
  quantite?: number | null;
  date?: dayjs.Dayjs | null;
  description?: string | null;
  etablissement?: IEtablissement | null;
  matiere?: IMatiere | null;
}

export class Bon implements IBon {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public date?: dayjs.Dayjs | null,
    public description?: string | null,
    public etablissement?: IEtablissement | null,
    public matiere?: IMatiere | null
  ) {}
}

export function getBonIdentifier(bon: IBon): number | undefined {
  return bon.id;
}
