import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InspectionDetailComponent } from './inspection-detail.component';

describe('Inspection Management Detail Component', () => {
  let comp: InspectionDetailComponent;
  let fixture: ComponentFixture<InspectionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InspectionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inspection: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InspectionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InspectionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inspection on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inspection).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
