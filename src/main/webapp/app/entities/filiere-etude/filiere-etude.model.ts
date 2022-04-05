import { IDiplome } from 'app/entities/diplome/diplome.model';
import { IReleveNote } from 'app/entities/releve-note/releve-note.model';
import { Filiere } from 'app/entities/enumerations/filiere.model';

export interface IFiliereEtude {
  id?: number;
  filiere?: Filiere;
  diplomes?: IDiplome[] | null;
  releveNotes?: IReleveNote[] | null;
}

export class FiliereEtude implements IFiliereEtude {
  constructor(
    public id?: number,
    public filiere?: Filiere,
    public diplomes?: IDiplome[] | null,
    public releveNotes?: IReleveNote[] | null
  ) {}
}

export function getFiliereEtudeIdentifier(filiereEtude: IFiliereEtude): number | undefined {
  return filiereEtude.id;
}
