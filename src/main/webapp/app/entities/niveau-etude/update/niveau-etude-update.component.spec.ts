import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NiveauEtudeService } from '../service/niveau-etude.service';
import { INiveauEtude, NiveauEtude } from '../niveau-etude.model';

import { NiveauEtudeUpdateComponent } from './niveau-etude-update.component';

describe('NiveauEtude Management Update Component', () => {
  let comp: NiveauEtudeUpdateComponent;
  let fixture: ComponentFixture<NiveauEtudeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let niveauEtudeService: NiveauEtudeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NiveauEtudeUpdateComponent],
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
      .overrideTemplate(NiveauEtudeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NiveauEtudeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    niveauEtudeService = TestBed.inject(NiveauEtudeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const niveauEtude: INiveauEtude = { id: 456 };

      activatedRoute.data = of({ niveauEtude });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(niveauEtude));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NiveauEtude>>();
      const niveauEtude = { id: 123 };
      jest.spyOn(niveauEtudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ niveauEtude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: niveauEtude }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(niveauEtudeService.update).toHaveBeenCalledWith(niveauEtude);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NiveauEtude>>();
      const niveauEtude = new NiveauEtude();
      jest.spyOn(niveauEtudeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ niveauEtude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: niveauEtude }));
      saveSubject.complete();

      // THEN
      expect(niveauEtudeService.create).toHaveBeenCalledWith(niveauEtude);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NiveauEtude>>();
      const niveauEtude = { id: 123 };
      jest.spyOn(niveauEtudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ niveauEtude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(niveauEtudeService.update).toHaveBeenCalledWith(niveauEtude);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
