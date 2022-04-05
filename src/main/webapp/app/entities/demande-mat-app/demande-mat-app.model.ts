import dayjs from 'dayjs/esm';

export interface IDemandeMatApp {
  id?: number;
  objet?: string | null;
  description?: string | null;
  dateDemande?: dayjs.Dayjs | null;
}

export class DemandeMatApp implements IDemandeMatApp {
  constructor(
    public id?: number,
    public objet?: string | null,
    public description?: string | null,
    public dateDemande?: dayjs.Dayjs | null
  ) {}
}

export function getDemandeMatAppIdentifier(demandeMatApp: IDemandeMatApp): number | undefined {
  return demandeMatApp.id;
}
