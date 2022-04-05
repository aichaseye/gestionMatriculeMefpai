import { IDiplome } from 'app/entities/diplome/diplome.model';
import { IReleveNote } from 'app/entities/releve-note/releve-note.model';
import { Serie } from 'app/entities/enumerations/serie.model';

export interface ISerieEtude {
  id?: number;
  serie?: Serie;
  diplomes?: IDiplome[] | null;
  releveNotes?: IReleveNote[] | null;
}

export class SerieEtude implements ISerieEtude {
  constructor(public id?: number, public serie?: Serie, public diplomes?: IDiplome[] | null, public releveNotes?: IReleveNote[] | null) {}
}

export function getSerieEtudeIdentifier(serieEtude: ISerieEtude): number | undefined {
  return serieEtude.id;
}
