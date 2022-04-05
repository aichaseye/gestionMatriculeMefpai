import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaysDetailComponent } from './pays-detail.component';

describe('Pays Management Detail Component', () => {
  let comp: PaysDetailComponent;
  let fixture: ComponentFixture<PaysDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaysDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pays: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PaysDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PaysDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pays on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pays).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
