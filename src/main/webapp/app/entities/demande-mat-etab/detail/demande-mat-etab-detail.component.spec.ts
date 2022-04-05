import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DemandeMatEtabDetailComponent } from './demande-mat-etab-detail.component';

describe('DemandeMatEtab Management Detail Component', () => {
  let comp: DemandeMatEtabDetailComponent;
  let fixture: ComponentFixture<DemandeMatEtabDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DemandeMatEtabDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ demandeMatEtab: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DemandeMatEtabDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DemandeMatEtabDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load demandeMatEtab on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.demandeMatEtab).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
