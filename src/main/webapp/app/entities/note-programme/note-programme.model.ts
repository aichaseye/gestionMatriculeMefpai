import { IReleveNote } from 'app/entities/releve-note/releve-note.model';

export interface INoteProgramme {
  id?: number;
  nomProg?: string | null;
  coef?: number | null;
  note?: number | null;
  releveNotes?: IReleveNote[] | null;
}

export class NoteProgramme implements INoteProgramme {
  constructor(
    public id?: number,
    public nomProg?: string | null,
    public coef?: number | null,
    public note?: number | null,
    public releveNotes?: IReleveNote[] | null
  ) {}
}

export function getNoteProgrammeIdentifier(noteProgramme: INoteProgramme): number | undefined {
  return noteProgramme.id;
}
