import { IInscription } from 'app/entities/inscription/inscription.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';

export interface IClasse {
  id?: number;
  nomClasse?: string;
  inscriptions?: IInscription[] | null;
  etablissement?: IEtablissement | null;
}

export class Classe implements IClasse {
  constructor(
    public id?: number,
    public nomClasse?: string,
    public inscriptions?: IInscription[] | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getClasseIdentifier(classe: IClasse): number | undefined {
  return classe.id;
}
