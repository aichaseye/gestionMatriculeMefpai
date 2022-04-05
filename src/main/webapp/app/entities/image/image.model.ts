import { IMatiere } from 'app/entities/matiere/matiere.model';

export interface IImage {
  id?: number;
  url?: string | null;
  matiere?: IMatiere | null;
}

export class Image implements IImage {
  constructor(public id?: number, public url?: string | null, public matiere?: IMatiere | null) {}
}

export function getImageIdentifier(image: IImage): number | undefined {
  return image.id;
}
