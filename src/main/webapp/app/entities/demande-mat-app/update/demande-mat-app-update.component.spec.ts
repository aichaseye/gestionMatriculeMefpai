import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemandeMatAppService } from '../service/demande-mat-app.service';
import { IDemandeMatApp, DemandeMatApp } from '../demande-mat-app.model';

import { DemandeMatAppUpdateComponent } from './demande-mat-app-update.component';

describe('DemandeMatApp Management Update Component', () => {
  let comp: DemandeMatAppUpdateComponent;
  let fixture: ComponentFixture<DemandeMatAppUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demandeMatAppService: DemandeMatAppService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DemandeMatAppUpdateComponent],
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
      .overrideTemplate(DemandeMatAppUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeMatAppUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeMatAppService = TestBed.inject(DemandeMatAppService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const demandeMatApp: IDemandeMatApp = { id: 456 };

      activatedRoute.data = of({ demandeMatApp });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(demandeMatApp));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeMatApp>>();
      const demandeMatApp = { id: 123 };
      jest.spyOn(demandeMatAppService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeMatApp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeMatApp }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeMatAppService.update).toHaveBeenCalledWith(demandeMatApp);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeMatApp>>();
      const demandeMatApp = new DemandeMatApp();
      jest.spyOn(demandeMatAppService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeMatApp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeMatApp }));
      saveSubject.complete();

      // THEN
      expect(demandeMatAppService.create).toHaveBeenCalledWith(demandeMatApp);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeMatApp>>();
      const demandeMatApp = { id: 123 };
      jest.spyOn(demandeMatAppService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeMatApp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeMatAppService.update).toHaveBeenCalledWith(demandeMatApp);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
