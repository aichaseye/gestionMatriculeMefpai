import dayjs from 'dayjs/esm';
import { IDemandeMatApp } from 'app/entities/demande-mat-app/demande-mat-app.model';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { IReleveNote } from 'app/entities/releve-note/releve-note.model';
import { IDiplome } from 'app/entities/diplome/diplome.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { StatutApp } from 'app/entities/enumerations/statut-app.model';

export interface IApprenant {
  id?: number;
  nomCompletApp?: string;
  email?: string;
  telephone?: string;
  dateNaissance?: dayjs.Dayjs | null;
  lieuNaissance?: string | null;
  numActeNaissance?: number | null;
  photoContentType?: string | null;
  photo?: string | null;
  sexe?: Sexe;
  adresse?: string;
  statut?: StatutApp | null;
  situationMatrimoniale?: string | null;
  matriculeApp?: string | null;
  nationalite?: string;
  demandeMatApp?: IDemandeMatApp | null;
  inscriptions?: IInscription[] | null;
  releveNotes?: IReleveNote[] | null;
  diplomes?: IDiplome[] | null;
}

export class Apprenant implements IApprenant {
  constructor(
    public id?: number,
    public nomCompletApp?: string,
    public email?: string,
    public telephone?: string,
    public dateNaissance?: dayjs.Dayjs | null,
    public lieuNaissance?: string | null,
    public numActeNaissance?: number | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public sexe?: Sexe,
    public adresse?: string,
    public statut?: StatutApp | null,
    public situationMatrimoniale?: string | null,
    public matriculeApp?: string | null,
    public nationalite?: string,
    public demandeMatApp?: IDemandeMatApp | null,
    public inscriptions?: IInscription[] | null,
    public releveNotes?: IReleveNote[] | null,
    public diplomes?: IDiplome[] | null
  ) {}
}

export function getApprenantIdentifier(apprenant: IApprenant): number | undefined {
  return apprenant.id;
}
