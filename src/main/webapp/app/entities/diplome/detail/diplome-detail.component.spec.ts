import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DiplomeDetailComponent } from './diplome-detail.component';

describe('Diplome Management Detail Component', () => {
  let comp: DiplomeDetailComponent;
  let fixture: ComponentFixture<DiplomeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DiplomeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ diplome: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DiplomeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DiplomeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load diplome on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.diplome).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
