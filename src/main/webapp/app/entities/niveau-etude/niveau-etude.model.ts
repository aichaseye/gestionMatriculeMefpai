import { IDiplome } from 'app/entities/diplome/diplome.model';
import { IReleveNote } from 'app/entities/releve-note/releve-note.model';
import { Niveau } from 'app/entities/enumerations/niveau.model';

export interface INiveauEtude {
  id?: number;
  niveau?: Niveau;
  diplomes?: IDiplome[] | null;
  releveNotes?: IReleveNote[] | null;
}

export class NiveauEtude implements INiveauEtude {
  constructor(public id?: number, public niveau?: Niveau, public diplomes?: IDiplome[] | null, public releveNotes?: IReleveNote[] | null) {}
}

export function getNiveauEtudeIdentifier(niveauEtude: INiveauEtude): number | undefined {
  return niveauEtude.id;
}
