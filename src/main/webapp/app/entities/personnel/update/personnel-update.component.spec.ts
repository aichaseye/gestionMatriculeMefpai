import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PersonnelService } from '../service/personnel.service';
import { IPersonnel, Personnel } from '../personnel.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { IInspection } from 'app/entities/inspection/inspection.model';
import { InspectionService } from 'app/entities/inspection/service/inspection.service';
import { IPoste } from 'app/entities/poste/poste.model';
import { PosteService } from 'app/entities/poste/service/poste.service';

import { PersonnelUpdateComponent } from './personnel-update.component';

describe('Personnel Management Update Component', () => {
  let comp: PersonnelUpdateComponent;
  let fixture: ComponentFixture<PersonnelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personnelService: PersonnelService;
  let etablissementService: EtablissementService;
  let inspectionService: InspectionService;
  let posteService: PosteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PersonnelUpdateComponent],
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
      .overrideTemplate(PersonnelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonnelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personnelService = TestBed.inject(PersonnelService);
    etablissementService = TestBed.inject(EtablissementService);
    inspectionService = TestBed.inject(InspectionService);
    posteService = TestBed.inject(PosteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Etablissement query and add missing value', () => {
      const personnel: IPersonnel = { id: 456 };
      const etablissement: IEtablissement = { id: 84497 };
      personnel.etablissement = etablissement;

      const etablissementCollection: IEtablissement[] = [{ id: 54676 }];
      jest.spyOn(etablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: etablissementCollection })));
      const additionalEtablissements = [etablissement];
      const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
      jest.spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personnel });
      comp.ngOnInit();

      expect(etablissementService.query).toHaveBeenCalled();
      expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
        etablissementCollection,
        ...additionalEtablissements
      );
      expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Inspection query and add missing value', () => {
      const personnel: IPersonnel = { id: 456 };
      const inspection: IInspection = { id: 66747 };
      personnel.inspection = inspection;

      const inspectionCollection: IInspection[] = [{ id: 94143 }];
      jest.spyOn(inspectionService, 'query').mockReturnValue(of(new HttpResponse({ body: inspectionCollection })));
      const additionalInspections = [inspection];
      const expectedCollection: IInspection[] = [...additionalInspections, ...inspectionCollection];
      jest.spyOn(inspectionService, 'addInspectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personnel });
      comp.ngOnInit();

      expect(inspectionService.query).toHaveBeenCalled();
      expect(inspectionService.addInspectionToCollectionIfMissing).toHaveBeenCalledWith(inspectionCollection, ...additionalInspections);
      expect(comp.inspectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Poste query and add missing value', () => {
      const personnel: IPersonnel = { id: 456 };
      const poste: IPoste = { id: 49494 };
      personnel.poste = poste;

      const posteCollection: IPoste[] = [{ id: 59155 }];
      jest.spyOn(posteService, 'query').mockReturnValue(of(new HttpResponse({ body: posteCollection })));
      const additionalPostes = [poste];
      const expectedCollection: IPoste[] = [...additionalPostes, ...posteCollection];
      jest.spyOn(posteService, 'addPosteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personnel });
      comp.ngOnInit();

      expect(posteService.query).toHaveBeenCalled();
      expect(posteService.addPosteToCollectionIfMissing).toHaveBeenCalledWith(posteCollection, ...additionalPostes);
      expect(comp.postesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const personnel: IPersonnel = { id: 456 };
      const etablissement: IEtablissement = { id: 19208 };
      personnel.etablissement = etablissement;
      const inspection: IInspection = { id: 22484 };
      personnel.inspection = inspection;
      const poste: IPoste = { id: 92653 };
      personnel.poste = poste;

      activatedRoute.data = of({ personnel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(personnel));
      expect(comp.etablissementsSharedCollection).toContain(etablissement);
      expect(comp.inspectionsSharedCollection).toContain(inspection);
      expect(comp.postesSharedCollection).toContain(poste);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Personnel>>();
      const personnel = { id: 123 };
      jest.spyOn(personnelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personnel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personnel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(personnelService.update).toHaveBeenCalledWith(personnel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Personnel>>();
      const personnel = new Personnel();
      jest.spyOn(personnelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personnel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personnel }));
      saveSubject.complete();

      // THEN
      expect(personnelService.create).toHaveBeenCalledWith(personnel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Personnel>>();
      const personnel = { id: 123 };
      jest.spyOn(personnelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personnel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personnelService.update).toHaveBeenCalledWith(personnel);
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

    describe('trackInspectionById', () => {
      it('Should return tracked Inspection primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInspectionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPosteById', () => {
      it('Should return tracked Poste primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPosteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
