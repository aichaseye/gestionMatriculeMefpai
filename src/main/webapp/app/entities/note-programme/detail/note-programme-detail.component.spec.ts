import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NoteProgrammeDetailComponent } from './note-programme-detail.component';

describe('NoteProgramme Management Detail Component', () => {
  let comp: NoteProgrammeDetailComponent;
  let fixture: ComponentFixture<NoteProgrammeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NoteProgrammeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ noteProgramme: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NoteProgrammeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NoteProgrammeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load noteProgramme on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.noteProgramme).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
