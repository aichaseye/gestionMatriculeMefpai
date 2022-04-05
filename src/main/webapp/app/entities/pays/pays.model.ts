import { IRegion } from 'app/entities/region/region.model';
import { NomPays } from 'app/entities/enumerations/nom-pays.model';

export interface IPays {
  id?: number;
  nomPays?: NomPays;
  regions?: IRegion[] | null;
}

export class Pays implements IPays {
  constructor(public id?: number, public nomPays?: NomPays, public regions?: IRegion[] | null) {}
}

export function getPaysIdentifier(pays: IPays): number | undefined {
  return pays.id;
}
