import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SerieEtudeDetailComponent } from './serie-etude-detail.component';

describe('SerieEtude Management Detail Component', () => {
  let comp: SerieEtudeDetailComponent;
  let fixture: ComponentFixture<SerieEtudeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SerieEtudeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ serieEtude: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SerieEtudeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SerieEtudeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load serieEtude on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.serieEtude).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
