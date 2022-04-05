import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FiliereEtudeService } from '../service/filiere-etude.service';
import { IFiliereEtude, FiliereEtude } from '../filiere-etude.model';

import { FiliereEtudeUpdateComponent } from './filiere-etude-update.component';

describe('FiliereEtude Management Update Component', () => {
  let comp: FiliereEtudeUpdateComponent;
  let fixture: ComponentFixture<FiliereEtudeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let filiereEtudeService: FiliereEtudeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FiliereEtudeUpdateComponent],
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
      .overrideTemplate(FiliereEtudeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FiliereEtudeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    filiereEtudeService = TestBed.inject(FiliereEtudeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const filiereEtude: IFiliereEtude = { id: 456 };

      activatedRoute.data = of({ filiereEtude });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(filiereEtude));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FiliereEtude>>();
      const filiereEtude = { id: 123 };
      jest.spyOn(filiereEtudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiereEtude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filiereEtude }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(filiereEtudeService.update).toHaveBeenCalledWith(filiereEtude);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FiliereEtude>>();
      const filiereEtude = new FiliereEtude();
      jest.spyOn(filiereEtudeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiereEtude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filiereEtude }));
      saveSubject.complete();

      // THEN
      expect(filiereEtudeService.create).toHaveBeenCalledWith(filiereEtude);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FiliereEtude>>();
      const filiereEtude = { id: 123 };
      jest.spyOn(filiereEtudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiereEtude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(filiereEtudeService.update).toHaveBeenCalledWith(filiereEtude);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
