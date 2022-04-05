import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { IInspection } from 'app/entities/inspection/inspection.model';
import { IPoste } from 'app/entities/poste/poste.model';

export interface IPersonnel {
  id?: number;
  nomPers?: string;
  prenomPers?: string;
  email?: string;
  etablissement?: IEtablissement | null;
  inspection?: IInspection | null;
  poste?: IPoste | null;
}

export class Personnel implements IPersonnel {
  constructor(
    public id?: number,
    public nomPers?: string,
    public prenomPers?: string,
    public email?: string,
    public etablissement?: IEtablissement | null,
    public inspection?: IInspection | null,
    public poste?: IPoste | null
  ) {}
}

export function getPersonnelIdentifier(personnel: IPersonnel): number | undefined {
  return personnel.id;
}
