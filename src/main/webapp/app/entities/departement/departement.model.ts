import { ICommune } from 'app/entities/commune/commune.model';
import { IRegion } from 'app/entities/region/region.model';
import { NomDep } from 'app/entities/enumerations/nom-dep.model';

export interface IDepartement {
  id?: number;
  nomDep?: NomDep;
  communes?: ICommune[] | null;
  region?: IRegion | null;
}

export class Departement implements IDepartement {
  constructor(public id?: number, public nomDep?: NomDep, public communes?: ICommune[] | null, public region?: IRegion | null) {}
}

export function getDepartementIdentifier(departement: IDepartement): number | undefined {
  return departement.id;
}
