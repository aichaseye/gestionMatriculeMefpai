import { IPersonnel } from 'app/entities/personnel/personnel.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { ICommune } from 'app/entities/commune/commune.model';
import { TypeInspection } from 'app/entities/enumerations/type-inspection.model';

export interface IInspection {
  id?: number;
  nomInsp?: string;
  typeInsp?: TypeInspection;
  latitude?: number | null;
  longitude?: number | null;
  personnel?: IPersonnel[] | null;
  etablissements?: IEtablissement[] | null;
  commune?: ICommune | null;
}

export class Inspection implements IInspection {
  constructor(
    public id?: number,
    public nomInsp?: string,
    public typeInsp?: TypeInspection,
    public latitude?: number | null,
    public longitude?: number | null,
    public personnel?: IPersonnel[] | null,
    public etablissements?: IEtablissement[] | null,
    public commune?: ICommune | null
  ) {}
}

export function getInspectionIdentifier(inspection: IInspection): number | undefined {
  return inspection.id;
}
