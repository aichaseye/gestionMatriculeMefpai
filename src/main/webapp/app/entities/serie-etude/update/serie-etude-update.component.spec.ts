import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SerieEtudeService } from '../service/serie-etude.service';
import { ISerieEtude, SerieEtude } from '../serie-etude.model';

import { SerieEtudeUpdateComponent } from './serie-etude-update.component';

describe('SerieEtude Management Update Component', () => {
  let comp: SerieEtudeUpdateComponent;
  let fixture: ComponentFixture<SerieEtudeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serieEtudeService: SerieEtudeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SerieEtudeUpdateComponent],
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
      .overrideTemplate(SerieEtudeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SerieEtudeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serieEtudeService = TestBed.inject(SerieEtudeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const serieEtude: ISerieEtude = { id: 456 };

      activatedRoute.data = of({ serieEtude });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(serieEtude));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SerieEtude>>();
      const serieEtude = { id: 123 };
      jest.spyOn(serieEtudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serieEtude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serieEtude }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(serieEtudeService.update).toHaveBeenCalledWith(serieEtude);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SerieEtude>>();
      const serieEtude = new SerieEtude();
      jest.spyOn(serieEtudeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serieEtude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serieEtude }));
      saveSubject.complete();

      // THEN
      expect(serieEtudeService.create).toHaveBeenCalledWith(serieEtude);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SerieEtude>>();
      const serieEtude = { id: 123 };
      jest.spyOn(serieEtudeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serieEtude });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serieEtudeService.update).toHaveBeenCalledWith(serieEtude);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
