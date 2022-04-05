import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemandeMatEtabService } from '../service/demande-mat-etab.service';
import { IDemandeMatEtab, DemandeMatEtab } from '../demande-mat-etab.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { DemandeMatEtabUpdateComponent } from './demande-mat-etab-update.component';

describe('DemandeMatEtab Management Update Component', () => {
  let comp: DemandeMatEtabUpdateComponent;
  let fixture: ComponentFixture<DemandeMatEtabUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demandeMatEtabService: DemandeMatEtabService;
  let etablissementService: EtablissementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DemandeMatEtabUpdateComponent],
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
      .overrideTemplate(DemandeMatEtabUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeMatEtabUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeMatEtabService = TestBed.inject(DemandeMatEtabService);
    etablissementService = TestBed.inject(EtablissementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call etablissement query and add missing value', () => {
      const demandeMatEtab: IDemandeMatEtab = { id: 456 };
      const etablissement: IEtablissement = { id: 68830 };
      demandeMatEtab.etablissement = etablissement;

      const etablissementCollection: IEtablissement[] = [{ id: 31714 }];
      jest.spyOn(etablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: etablissementCollection })));
      const expectedCollection: IEtablissement[] = [etablissement, ...etablissementCollection];
      jest.spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeMatEtab });
      comp.ngOnInit();

      expect(etablissementService.query).toHaveBeenCalled();
      expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(etablissementCollection, etablissement);
      expect(comp.etablissementsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const demandeMatEtab: IDemandeMatEtab = { id: 456 };
      const etablissement: IEtablissement = { id: 52041 };
      demandeMatEtab.etablissement = etablissement;

      activatedRoute.data = of({ demandeMatEtab });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(demandeMatEtab));
      expect(comp.etablissementsCollection).toContain(etablissement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeMatEtab>>();
      const demandeMatEtab = { id: 123 };
      jest.spyOn(demandeMatEtabService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeMatEtab });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeMatEtab }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeMatEtabService.update).toHaveBeenCalledWith(demandeMatEtab);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeMatEtab>>();
      const demandeMatEtab = new DemandeMatEtab();
      jest.spyOn(demandeMatEtabService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeMatEtab });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeMatEtab }));
      saveSubject.complete();

      // THEN
      expect(demandeMatEtabService.create).toHaveBeenCalledWith(demandeMatEtab);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeMatEtab>>();
      const demandeMatEtab = { id: 123 };
      jest.spyOn(demandeMatEtabService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeMatEtab });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeMatEtabService.update).toHaveBeenCalledWith(demandeMatEtab);
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
  });
});
