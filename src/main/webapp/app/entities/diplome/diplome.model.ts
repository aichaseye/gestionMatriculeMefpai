import dayjs from 'dayjs/esm';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { IFiliereEtude } from 'app/entities/filiere-etude/filiere-etude.model';
import { ISerieEtude } from 'app/entities/serie-etude/serie-etude.model';
import { INiveauEtude } from 'app/entities/niveau-etude/niveau-etude.model';
import { NomDiplome } from 'app/entities/enumerations/nom-diplome.model';

export interface IDiplome {
  id?: number;
  nom?: NomDiplome;
  matriculeDiplome?: string;
  annee?: dayjs.Dayjs | null;
  apprenant?: IApprenant | null;
  filiereEtude?: IFiliereEtude | null;
  serieEtude?: ISerieEtude | null;
  niveauEtude?: INiveauEtude | null;
}

export class Diplome implements IDiplome {
  constructor(
    public id?: number,
    public nom?: NomDiplome,
    public matriculeDiplome?: string,
    public annee?: dayjs.Dayjs | null,
    public apprenant?: IApprenant | null,
    public filiereEtude?: IFiliereEtude | null,
    public serieEtude?: ISerieEtude | null,
    public niveauEtude?: INiveauEtude | null
  ) {}
}

export function getDiplomeIdentifier(diplome: IDiplome): number | undefined {
  return diplome.id;
}
