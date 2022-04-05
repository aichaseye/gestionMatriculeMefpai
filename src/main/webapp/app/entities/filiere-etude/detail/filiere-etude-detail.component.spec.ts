import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FiliereEtudeDetailComponent } from './filiere-etude-detail.component';

describe('FiliereEtude Management Detail Component', () => {
  let comp: FiliereEtudeDetailComponent;
  let fixture: ComponentFixture<FiliereEtudeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FiliereEtudeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ filiereEtude: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FiliereEtudeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FiliereEtudeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load filiereEtude on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.filiereEtude).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
