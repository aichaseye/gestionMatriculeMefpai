import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NoteProgrammeService } from '../service/note-programme.service';
import { INoteProgramme, NoteProgramme } from '../note-programme.model';
import { IReleveNote } from 'app/entities/releve-note/releve-note.model';
import { ReleveNoteService } from 'app/entities/releve-note/service/releve-note.service';

import { NoteProgrammeUpdateComponent } from './note-programme-update.component';

describe('NoteProgramme Management Update Component', () => {
  let comp: NoteProgrammeUpdateComponent;
  let fixture: ComponentFixture<NoteProgrammeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let noteProgrammeService: NoteProgrammeService;
  let releveNoteService: ReleveNoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NoteProgrammeUpdateComponent],
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
      .overrideTemplate(NoteProgrammeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NoteProgrammeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    noteProgrammeService = TestBed.inject(NoteProgrammeService);
    releveNoteService = TestBed.inject(ReleveNoteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ReleveNote query and add missing value', () => {
      const noteProgramme: INoteProgramme = { id: 456 };
      const releveNotes: IReleveNote[] = [{ id: 70358 }];
      noteProgramme.releveNotes = releveNotes;

      const releveNoteCollection: IReleveNote[] = [{ id: 33318 }];
      jest.spyOn(releveNoteService, 'query').mockReturnValue(of(new HttpResponse({ body: releveNoteCollection })));
      const additionalReleveNotes = [...releveNotes];
      const expectedCollection: IReleveNote[] = [...additionalReleveNotes, ...releveNoteCollection];
      jest.spyOn(releveNoteService, 'addReleveNoteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ noteProgramme });
      comp.ngOnInit();

      expect(releveNoteService.query).toHaveBeenCalled();
      expect(releveNoteService.addReleveNoteToCollectionIfMissing).toHaveBeenCalledWith(releveNoteCollection, ...additionalReleveNotes);
      expect(comp.releveNotesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const noteProgramme: INoteProgramme = { id: 456 };
      const releveNotes: IReleveNote = { id: 98193 };
      noteProgramme.releveNotes = [releveNotes];

      activatedRoute.data = of({ noteProgramme });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(noteProgramme));
      expect(comp.releveNotesSharedCollection).toContain(releveNotes);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NoteProgramme>>();
      const noteProgramme = { id: 123 };
      jest.spyOn(noteProgrammeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ noteProgramme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: noteProgramme }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(noteProgrammeService.update).toHaveBeenCalledWith(noteProgramme);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NoteProgramme>>();
      const noteProgramme = new NoteProgramme();
      jest.spyOn(noteProgrammeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ noteProgramme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: noteProgramme }));
      saveSubject.complete();

      // THEN
      expect(noteProgrammeService.create).toHaveBeenCalledWith(noteProgramme);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<NoteProgramme>>();
      const noteProgramme = { id: 123 };
      jest.spyOn(noteProgrammeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ noteProgramme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(noteProgrammeService.update).toHaveBeenCalledWith(noteProgramme);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackReleveNoteById', () => {
      it('Should return tracked ReleveNote primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackReleveNoteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedReleveNote', () => {
      it('Should return option if no ReleveNote is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedReleveNote(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected ReleveNote for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedReleveNote(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this ReleveNote is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedReleveNote(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
