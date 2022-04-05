import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApprenantService } from '../service/apprenant.service';
import { IApprenant, Apprenant } from '../apprenant.model';
import { IDemandeMatApp } from 'app/entities/demande-mat-app/demande-mat-app.model';
import { DemandeMatAppService } from 'app/entities/demande-mat-app/service/demande-mat-app.service';

import { ApprenantUpdateComponent } from './apprenant-update.component';

describe('Apprenant Management Update Component', () => {
  let comp: ApprenantUpdateComponent;
  let fixture: ComponentFixture<ApprenantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apprenantService: ApprenantService;
  let demandeMatAppService: DemandeMatAppService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApprenantUpdateComponent],
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
      .overrideTemplate(ApprenantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApprenantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apprenantService = TestBed.inject(ApprenantService);
    demandeMatAppService = TestBed.inject(DemandeMatAppService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call demandeMatApp query and add missing value', () => {
      const apprenant: IApprenant = { id: 456 };
      const demandeMatApp: IDemandeMatApp = { id: 44276 };
      apprenant.demandeMatApp = demandeMatApp;

      const demandeMatAppCollection: IDemandeMatApp[] = [{ id: 27241 }];
      jest.spyOn(demandeMatAppService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeMatAppCollection })));
      const expectedCollection: IDemandeMatApp[] = [demandeMatApp, ...demandeMatAppCollection];
      jest.spyOn(demandeMatAppService, 'addDemandeMatAppToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      expect(demandeMatAppService.query).toHaveBeenCalled();
      expect(demandeMatAppService.addDemandeMatAppToCollectionIfMissing).toHaveBeenCalledWith(demandeMatAppCollection, demandeMatApp);
      expect(comp.demandeMatAppsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const apprenant: IApprenant = { id: 456 };
      const demandeMatApp: IDemandeMatApp = { id: 55731 };
      apprenant.demandeMatApp = demandeMatApp;

      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(apprenant));
      expect(comp.demandeMatAppsCollection).toContain(demandeMatApp);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apprenant>>();
      const apprenant = { id: 123 };
      jest.spyOn(apprenantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apprenant }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(apprenantService.update).toHaveBeenCalledWith(apprenant);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apprenant>>();
      const apprenant = new Apprenant();
      jest.spyOn(apprenantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apprenant }));
      saveSubject.complete();

      // THEN
      expect(apprenantService.create).toHaveBeenCalledWith(apprenant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apprenant>>();
      const apprenant = { id: 123 };
      jest.spyOn(apprenantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apprenant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apprenantService.update).toHaveBeenCalledWith(apprenant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDemandeMatAppById', () => {
      it('Should return tracked DemandeMatApp primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDemandeMatAppById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
