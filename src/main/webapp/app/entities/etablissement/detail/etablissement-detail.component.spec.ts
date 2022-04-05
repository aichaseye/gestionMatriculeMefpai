import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EtablissementDetailComponent } from './etablissement-detail.component';

describe('Etablissement Management Detail Component', () => {
  let comp: EtablissementDetailComponent;
  let fixture: ComponentFixture<EtablissementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EtablissementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ etablissement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EtablissementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EtablissementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load etablissement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.etablissement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
