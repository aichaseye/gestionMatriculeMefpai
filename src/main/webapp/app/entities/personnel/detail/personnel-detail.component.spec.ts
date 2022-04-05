import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonnelDetailComponent } from './personnel-detail.component';

describe('Personnel Management Detail Component', () => {
  let comp: PersonnelDetailComponent;
  let fixture: ComponentFixture<PersonnelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonnelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ personnel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PersonnelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonnelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load personnel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.personnel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
