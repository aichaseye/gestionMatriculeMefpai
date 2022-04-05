import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClasseDetailComponent } from './classe-detail.component';

describe('Classe Management Detail Component', () => {
  let comp: ClasseDetailComponent;
  let fixture: ComponentFixture<ClasseDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClasseDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ classe: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClasseDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClasseDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load classe on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.classe).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
