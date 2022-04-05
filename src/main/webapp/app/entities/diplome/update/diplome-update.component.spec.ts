import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DiplomeService } from '../service/diplome.service';
import { IDiplome, Diplome } from '../diplome.model';
import { IApprenant } from 'app/entities/apprenant/apprenant.model';
import { ApprenantService } from 'app/entities/apprenant/service/apprenant.service';
import { IFiliereEtude } from 'app/entities/filiere-etude/filiere-etude.model';
import { FiliereEtudeService } from 'app/entities/filiere-etude/service/filiere-etude.service';
import { ISerieEtude } from 'app/entities/serie-etude/serie-etude.model';
import { SerieEtudeService } from 'app/entities/serie-etude/service/serie-etude.service';
import { INiveauEtude } from 'app/entities/niveau-etude/niveau-etude.model';
import { NiveauEtudeService } from 'app/entities/niveau-etude/service/niveau-etude.service';

import { DiplomeUpdateComponent } from './diplome-update.component';

describe('Diplome Management Update Component', () => {
  let comp: DiplomeUpdateComponent;
  let fixture: ComponentFixture<DiplomeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let diplomeService: DiplomeService;
  let apprenantService: ApprenantService;
  let filiereEtudeService: FiliereEtudeService;
  let serieEtudeService: SerieEtudeService;
  let niveauEtudeService: NiveauEtudeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DiplomeUpdateComponent],
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
      .overrideTemplate(DiplomeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DiplomeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    diplomeService = TestBed.inject(DiplomeService);
    apprenantService = TestBed.inject(ApprenantService);
    filiereEtudeService = TestBed.inject(FiliereEtudeService);
    serieEtudeService = TestBed.inject(SerieEtudeService);
    niveauEtudeService = TestBed.inject(NiveauEtudeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Apprenant query and add missing value', () => {
      const diplome: IDiplome = { id: 456 };
      const apprenant: IApprenant = { id: 12737 };
      diplome.apprenant = apprenant;

      const apprenantCollection: IApprenant[] = [{ id: 12025 }];
      jest.spyOn(apprenantService, 'query').mockReturnValue(of(new HttpResponse({ body: apprenantCollection })));
      const additionalApprenants = [apprenant];
      const expectedCollection: IApprenant[] = [...additionalApprenants, ...apprenantCollection];
      jest.spyOn(apprenantService, 'addApprenantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ diplome });
      comp.ngOnInit();

      expect(apprenantService.query).toHaveBeenCalled();
      expect(apprenantService.addApprenantToCollectionIfMissing).toHaveBeenCalledWith(apprenantCollection, ...additionalApprenants);
      expect(comp.apprenantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FiliereEtude query and add missing value', () => {
      const diplome: IDiplome = { id: 456 };
      const filiereEtude: IFiliereEtude = { id: 20609 };
      diplome.filiereEtude = filiereEtude;

      const filiereEtudeCollection: IFiliereEtude[] = [{ id: 9583 }];
      jest.spyOn(filiereEtudeService, 'query').mockReturnValue(of(new HttpResponse({ body: filiereEtudeCollection })));
      const additionalFiliereEtudes = [filiereEtude];
      const expectedCollection: IFiliereEtude[] = [...additionalFiliereEtudes, ...filiereEtudeCollection];
      jest.spyOn(filiereEtudeService, 'addFiliereEtudeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ diplome });
      comp.ngOnInit();

      expect(filiereEtudeService.query).toHaveBeenCalled();
      expect(filiereEtudeService.addFiliereEtudeToCollectionIfMissing).toHaveBeenCalledWith(
        filiereEtudeCollection,
        ...additionalFiliereEtudes
      );
      expect(comp.filiereEtudesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SerieEtude query and add missing value', () => {
      const diplome: IDiplome = { id: 456 };
      const serieEtude: ISerieEtude = { id: 94396 };
      diplome.serieEtude = serieEtude;

      const serieEtudeCollection: ISerieEtude[] = [{ id: 72161 }];
      jest.spyOn(serieEtudeService, 'query').mockReturnValue(of(new HttpResponse({ body: serieEtudeCollection })));
      const additionalSerieEtudes = [serieEtude];
      const expectedCollection: ISerieEtude[] = [...additionalSerieEtudes, ...serieEtudeCollection];
      jest.spyOn(serieEtudeService, 'addSerieEtudeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ diplome });
      comp.ngOnInit();

      expect(serieEtudeService.query).toHaveBeenCalled();
      expect(serieEtudeService.addSerieEtudeToCollectionIfMissing).toHaveBeenCalledWith(serieEtudeCollection, ...additionalSerieEtudes);
      expect(comp.serieEtudesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call NiveauEtude query and add missing value', () => {
      const diplome: IDiplome = { id: 456 };
      const niveauEtude: INiveauEtude = { id: 64845 };
      diplome.niveauEtude = niveauEtude;

      const niveauEtudeCollection: INiveauEtude[] = [{ id: 95839 }];
      jest.spyOn(niveauEtudeService, 'query').mockReturnValue(of(new HttpResponse({ body: niveauEtudeCollection })));
      const additionalNiveauEtudes = [niveauEtude];
      const expectedCollection: INiveauEtude[] = [...additionalNiveauEtudes, ...niveauEtudeCollection];
      jest.spyOn(niveauEtudeService, 'addNiveauEtudeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ diplome });
      comp.ngOnInit();

      expect(niveauEtudeService.query).toHaveBeenCalled();
      expect(niveauEtudeService.addNiveauEtudeToCollectionIfMissing).toHaveBeenCalledWith(niveauEtudeCollection, ...additionalNiveauEtudes);
      expect(comp.niveauEtudesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const diplome: IDiplome = { id: 456 };
      const apprenant: IApprenant = { id: 69202 };
      diplome.apprenant = apprenant;
      const filiereEtude: IFiliereEtude = { id: 92444 };
      diplome.filiereEtude = filiereEtude;
      const serieEtude: ISerieEtude = { id: 1945 };
      diplome.serieEtude = serieEtude;
      const niveauEtude: INiveauEtude = { id: 37119 };
      diplome.niveauEtude = niveauEtude;

      activatedRoute.data = of({ diplome });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(diplome));
      expect(comp.apprenantsSharedCollection).toContain(apprenant);
      expect(comp.filiereEtudesSharedCollection).toContain(filiereEtude);
      expect(comp.serieEtudesSharedCollection).toContain(serieEtude);
      expect(comp.niveauEtudesSharedCollection).toContain(niveauEtude);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Diplome>>();
      const diplome = { id: 123 };
      jest.spyOn(diplomeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ diplome });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: diplome }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(diplomeService.update).toHaveBeenCalledWith(diplome);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Diplome>>();
      const diplome = new Diplome();
      jest.spyOn(diplomeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ diplome });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: diplome }));
      saveSubject.complete();

      // THEN
      expect(diplomeService.create).toHaveBeenCalledWith(diplome);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Diplome>>();
      const diplome = { id: 123 };
      jest.spyOn(diplomeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ diplome });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(diplomeService.update).toHaveBeenCalledWith(diplome);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackApprenantById', () => {
      it('Should return tracked Apprenant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApprenantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFiliereEtudeById', () => {
      it('Should return tracked FiliereEtude primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiliereEtudeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSerieEtudeById', () => {
      it('Should return tracked SerieEtude primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSerieEtudeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackNiveauEtudeById', () => {
      it('Should return tracked NiveauEtude primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackNiveauEtudeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
