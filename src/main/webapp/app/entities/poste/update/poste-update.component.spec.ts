import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PosteService } from '../service/poste.service';
import { IPoste, Poste } from '../poste.model';

import { PosteUpdateComponent } from './poste-update.component';

describe('Poste Management Update Component', () => {
  let comp: PosteUpdateComponent;
  let fixture: ComponentFixture<PosteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let posteService: PosteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PosteUpdateComponent],
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
      .overrideTemplate(PosteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PosteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    posteService = TestBed.inject(PosteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const poste: IPoste = { id: 456 };

      activatedRoute.data = of({ poste });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(poste));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Poste>>();
      const poste = { id: 123 };
      jest.spyOn(posteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: poste }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(posteService.update).toHaveBeenCalledWith(poste);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Poste>>();
      const poste = new Poste();
      jest.spyOn(posteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: poste }));
      saveSubject.complete();

      // THEN
      expect(posteService.create).toHaveBeenCalledWith(poste);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Poste>>();
      const poste = { id: 123 };
      jest.spyOn(posteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ poste });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(posteService.update).toHaveBeenCalledWith(poste);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
