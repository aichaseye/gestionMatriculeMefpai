import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReleveNoteDetailComponent } from './releve-note-detail.component';

describe('ReleveNote Management Detail Component', () => {
  let comp: ReleveNoteDetailComponent;
  let fixture: ComponentFixture<ReleveNoteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReleveNoteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ releveNote: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReleveNoteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReleveNoteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load releveNote on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.releveNote).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
