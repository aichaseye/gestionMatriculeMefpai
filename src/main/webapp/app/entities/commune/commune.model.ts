import { IInspection } from 'app/entities/inspection/inspection.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { IDepartement } from 'app/entities/departement/departement.model';

export interface ICommune {
  id?: number;
  nomCom?: string;
  inspections?: IInspection[] | null;
  etablissements?: IEtablissement[] | null;
  departement?: IDepartement | null;
}

export class Commune implements ICommune {
  constructor(
    public id?: number,
    public nomCom?: string,
    public inspections?: IInspection[] | null,
    public etablissements?: IEtablissement[] | null,
    public departement?: IDepartement | null
  ) {}
}

export function getCommuneIdentifier(commune: ICommune): number | undefined {
  return commune.id;
}
