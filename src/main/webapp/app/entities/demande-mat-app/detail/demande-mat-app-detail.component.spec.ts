import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DemandeMatAppDetailComponent } from './demande-mat-app-detail.component';

describe('DemandeMatApp Management Detail Component', () => {
  let comp: DemandeMatAppDetailComponent;
  let fixture: ComponentFixture<DemandeMatAppDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DemandeMatAppDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ demandeMatApp: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DemandeMatAppDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DemandeMatAppDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load demandeMatApp on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.demandeMatApp).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
