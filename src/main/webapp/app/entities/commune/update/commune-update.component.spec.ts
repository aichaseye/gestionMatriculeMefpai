import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommuneService } from '../service/commune.service';
import { ICommune, Commune } from '../commune.model';
import { IDepartement } from 'app/entities/departement/departement.model';
import { DepartementService } from 'app/entities/departement/service/departement.service';

import { CommuneUpdateComponent } from './commune-update.component';

describe('Commune Management Update Component', () => {
  let comp: CommuneUpdateComponent;
  let fixture: ComponentFixture<CommuneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let communeService: CommuneService;
  let departementService: DepartementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommuneUpdateComponent],
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
      .overrideTemplate(CommuneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommuneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    communeService = TestBed.inject(CommuneService);
    departementService = TestBed.inject(DepartementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Departement query and add missing value', () => {
      const commune: ICommune = { id: 456 };
      const departement: IDepartement = { id: 99092 };
      commune.departement = departement;

      const departementCollection: IDepartement[] = [{ id: 88413 }];
      jest.spyOn(departementService, 'query').mockReturnValue(of(new HttpResponse({ body: departementCollection })));
      const additionalDepartements = [departement];
      const expectedCollection: IDepartement[] = [...additionalDepartements, ...departementCollection];
      jest.spyOn(departementService, 'addDepartementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commune });
      comp.ngOnInit();

      expect(departementService.query).toHaveBeenCalled();
      expect(departementService.addDepartementToCollectionIfMissing).toHaveBeenCalledWith(departementCollection, ...additionalDepartements);
      expect(comp.departementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commune: ICommune = { id: 456 };
      const departement: IDepartement = { id: 55711 };
      commune.departement = departement;

      activatedRoute.data = of({ commune });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(commune));
      expect(comp.departementsSharedCollection).toContain(departement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commune>>();
      const commune = { id: 123 };
      jest.spyOn(communeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commune });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commune }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(communeService.update).toHaveBeenCalledWith(commune);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commune>>();
      const commune = new Commune();
      jest.spyOn(communeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commune });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commune }));
      saveSubject.complete();

      // THEN
      expect(communeService.create).toHaveBeenCalledWith(commune);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commune>>();
      const commune = { id: 123 };
      jest.spyOn(communeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commune });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(communeService.update).toHaveBeenCalledWith(commune);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDepartementById', () => {
      it('Should return tracked Departement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepartementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
