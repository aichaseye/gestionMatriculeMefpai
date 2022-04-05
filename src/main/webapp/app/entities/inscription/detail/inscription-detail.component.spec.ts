import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InscriptionDetailComponent } from './inscription-detail.component';

describe('Inscription Management Detail Component', () => {
  let comp: InscriptionDetailComponent;
  let fixture: ComponentFixture<InscriptionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InscriptionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inscription: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InscriptionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InscriptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inscription on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inscription).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
