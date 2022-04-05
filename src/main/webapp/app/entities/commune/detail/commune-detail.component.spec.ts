import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommuneDetailComponent } from './commune-detail.component';

describe('Commune Management Detail Component', () => {
  let comp: CommuneDetailComponent;
  let fixture: ComponentFixture<CommuneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CommuneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ commune: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CommuneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CommuneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load commune on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.commune).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
