import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InspectionService } from '../service/inspection.service';
import { IInspection, Inspection } from '../inspection.model';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';

import { InspectionUpdateComponent } from './inspection-update.component';

describe('Inspection Management Update Component', () => {
  let comp: InspectionUpdateComponent;
  let fixture: ComponentFixture<InspectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inspectionService: InspectionService;
  let communeService: CommuneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InspectionUpdateComponent],
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
      .overrideTemplate(InspectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inspectionService = TestBed.inject(InspectionService);
    communeService = TestBed.inject(CommuneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Commune query and add missing value', () => {
      const inspection: IInspection = { id: 456 };
      const commune: ICommune = { id: 17542 };
      inspection.commune = commune;

      const communeCollection: ICommune[] = [{ id: 11049 }];
      jest.spyOn(communeService, 'query').mockReturnValue(of(new HttpResponse({ body: communeCollection })));
      const additionalCommunes = [commune];
      const expectedCollection: ICommune[] = [...additionalCommunes, ...communeCollection];
      jest.spyOn(communeService, 'addCommuneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      expect(communeService.query).toHaveBeenCalled();
      expect(communeService.addCommuneToCollectionIfMissing).toHaveBeenCalledWith(communeCollection, ...additionalCommunes);
      expect(comp.communesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inspection: IInspection = { id: 456 };
      const commune: ICommune = { id: 12940 };
      inspection.commune = commune;

      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(inspection));
      expect(comp.communesSharedCollection).toContain(commune);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inspection>>();
      const inspection = { id: 123 };
      jest.spyOn(inspectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspection }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(inspectionService.update).toHaveBeenCalledWith(inspection);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inspection>>();
      const inspection = new Inspection();
      jest.spyOn(inspectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspection }));
      saveSubject.complete();

      // THEN
      expect(inspectionService.create).toHaveBeenCalledWith(inspection);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inspection>>();
      const inspection = { id: 123 };
      jest.spyOn(inspectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inspectionService.update).toHaveBeenCalledWith(inspection);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCommuneById', () => {
      it('Should return tracked Commune primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCommuneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
