import dayjs from 'dayjs/esm';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { IClasse } from 'app/entities/classe/classe.model';

export interface IInscription {
  id?: number;
  anneeIns?: dayjs.Dayjs;
  apprenant?: IApprenant | null;
  classe?: IClasse | null;
}

export class Inscription implements IInscription {
  constructor(public id?: number, public anneeIns?: dayjs.Dayjs, public apprenant?: IApprenant | null, public classe?: IClasse | null) {}
}

export function getInscriptionIdentifier(inscription: IInscription): number | undefined {
  return inscription.id;
}
