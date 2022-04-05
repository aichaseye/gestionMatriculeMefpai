import { IDepartement } from 'app/entities/departement/departement.model';
import { IPays } from 'app/entities/pays/pays.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';

export interface IRegion {
  id?: number;
  nomReg?: NomRegion;
  departements?: IDepartement[] | null;
  pays?: IPays | null;
}

export class Region implements IRegion {
  constructor(public id?: number, public nomReg?: NomRegion, public departements?: IDepartement[] | null, public pays?: IPays | null) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
