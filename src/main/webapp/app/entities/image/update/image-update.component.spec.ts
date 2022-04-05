import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ImageService } from '../service/image.service';
import { IImage, Image } from '../image.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

import { ImageUpdateComponent } from './image-update.component';

describe('Image Management Update Component', () => {
  let comp: ImageUpdateComponent;
  let fixture: ComponentFixture<ImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imageService: ImageService;
  let matiereService: MatiereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ImageUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imageService = TestBed.inject(ImageService);
    matiereService = TestBed.inject(MatiereService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Matiere query and add missing value', () => {
      const image: IImage = { id: 456 };
      const matiere: IMatiere = { id: 9324 };
      image.matiere = matiere;

      const matiereCollection: IMatiere[] = [{ id: 11995 }];
      jest.spyOn(matiereService, 'query').mockReturnValue(of(new HttpResponse({ body: matiereCollection })));
      const additionalMatieres = [matiere];
      const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
      jest.spyOn(matiereService, 'addMatiereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ image });
      comp.ngOnInit();

      expect(matiereService.query).toHaveBeenCalled();
      expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
      expect(comp.matieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const image: IImage = { id: 456 };
      const matiere: IMatiere = { id: 82191 };
      image.matiere = matiere;

      activatedRoute.data = of({ image });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(image));
      expect(comp.matieresSharedCollection).toContain(matiere);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Image>>();
      const image = { id: 123 };
      jest.spyOn(imageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ image });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: image }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(imageService.update).toHaveBeenCalledWith(image);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Image>>();
      const image = new Image();
      jest.spyOn(imageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ image });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: image }));
      saveSubject.complete();

      // THEN
      expect(imageService.create).toHaveBeenCalledWith(image);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Image>>();
      const image = { id: 123 };
      jest.spyOn(imageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ image });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imageService.update).toHaveBeenCalledWith(image);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMatiereById', () => {
      it('Should return tracked Matiere primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatiereById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
