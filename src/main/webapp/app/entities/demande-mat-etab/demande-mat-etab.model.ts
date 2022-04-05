import dayjs from 'dayjs/esm';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';

export interface IDemandeMatEtab {
  id?: number;
  objet?: string | null;
  description?: string | null;
  dateDemande?: dayjs.Dayjs | null;
  etablissement?: IEtablissement | null;
}

export class DemandeMatEtab implements IDemandeMatEtab {
  constructor(
    public id?: number,
    public objet?: string | null,
    public description?: string | null,
    public dateDemande?: dayjs.Dayjs | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getDemandeMatEtabIdentifier(demandeMatEtab: IDemandeMatEtab): number | undefined {
  return demandeMatEtab.id;
}
