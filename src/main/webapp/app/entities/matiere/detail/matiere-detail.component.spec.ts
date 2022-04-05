import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MatiereDetailComponent } from './matiere-detail.component';

describe('Matiere Management Detail Component', () => {
  let comp: MatiereDetailComponent;
  let fixture: ComponentFixture<MatiereDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MatiereDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ matiere: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MatiereDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MatiereDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load matiere on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.matiere).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
