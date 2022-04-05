import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BonDetailComponent } from './bon-detail.component';

describe('Bon Management Detail Component', () => {
  let comp: BonDetailComponent;
  let fixture: ComponentFixture<BonDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BonDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bon: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BonDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BonDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bon on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bon).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
