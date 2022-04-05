import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NiveauEtudeDetailComponent } from './niveau-etude-detail.component';

describe('NiveauEtude Management Detail Component', () => {
  let comp: NiveauEtudeDetailComponent;
  let fixture: ComponentFixture<NiveauEtudeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NiveauEtudeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ niveauEtude: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NiveauEtudeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NiveauEtudeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load niveauEtude on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.niveauEtude).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
