import { IPersonnel } from 'app/entities/personnel/personnel.model';
import { NomPoste } from 'app/entities/enumerations/nom-poste.model';

export interface IPoste {
  id?: number;
  nomPoste?: NomPoste;
  personnel?: IPersonnel[] | null;
}

export class Poste implements IPoste {
  constructor(public id?: number, public nomPoste?: NomPoste, public personnel?: IPersonnel[] | null) {}
}

export function getPosteIdentifier(poste: IPoste): number | undefined {
  return poste.id;
}
