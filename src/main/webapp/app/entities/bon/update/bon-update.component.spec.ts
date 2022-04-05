import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BonService } from '../service/bon.service';
import { IBon, Bon } from '../bon.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

import { BonUpdateComponent } from './bon-update.component';

describe('Bon Management Update Component', () => {
  let comp: BonUpdateComponent;
  let fixture: ComponentFixture<BonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bonService: BonService;
  let etablissementService: EtablissementService;
  let matiereService: MatiereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BonUpdateComponent],
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
      .overrideTemplate(BonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bonService = TestBed.inject(BonService);
    etablissementService = TestBed.inject(EtablissementService);
    matiereService = TestBed.inject(MatiereService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Etablissement query and add missing value', () => {
      const bon: IBon = { id: 456 };
      const etablissement: IEtablissement = { id: 26665 };
      bon.etablissement = etablissement;

      const etablissementCollection: IEtablissement[] = [{ id: 80708 }];
      jest.spyOn(etablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: etablissementCollection })));
      const additionalEtablissements = [etablissement];
      const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
      jest.spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bon });
      comp.ngOnInit();

      expect(etablissementService.query).toHaveBeenCalled();
      expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
        etablissementCollection,
        ...additionalEtablissements
      );
      expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Matiere query and add missing value', () => {
      const bon: IBon = { id: 456 };
      const matiere: IMatiere = { id: 653 };
      bon.matiere = matiere;

      const matiereCollection: IMatiere[] = [{ id: 12275 }];
      jest.spyOn(matiereService, 'query').mockReturnValue(of(new HttpResponse({ body: matiereCollection })));
      const additionalMatieres = [matiere];
      const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
      jest.spyOn(matiereService, 'addMatiereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bon });
      comp.ngOnInit();

      expect(matiereService.query).toHaveBeenCalled();
      expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
      expect(comp.matieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bon: IBon = { id: 456 };
      const etablissement: IEtablissement = { id: 6272 };
      bon.etablissement = etablissement;
      const matiere: IMatiere = { id: 53963 };
      bon.matiere = matiere;

      activatedRoute.data = of({ bon });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bon));
      expect(comp.etablissementsSharedCollection).toContain(etablissement);
      expect(comp.matieresSharedCollection).toContain(matiere);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bon>>();
      const bon = { id: 123 };
      jest.spyOn(bonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bon }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bonService.update).toHaveBeenCalledWith(bon);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bon>>();
      const bon = new Bon();
      jest.spyOn(bonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bon }));
      saveSubject.complete();

      // THEN
      expect(bonService.create).toHaveBeenCalledWith(bon);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bon>>();
      const bon = { id: 123 };
      jest.spyOn(bonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bonService.update).toHaveBeenCalledWith(bon);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEtablissementById', () => {
      it('Should return tracked Etablissement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEtablissementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMatiereById', () => {
      it('Should return tracked Matiere primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatiereById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
