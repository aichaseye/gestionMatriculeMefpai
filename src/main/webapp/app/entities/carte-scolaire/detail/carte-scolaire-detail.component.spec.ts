import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CarteScolaireDetailComponent } from './carte-scolaire-detail.component';

describe('CarteScolaire Management Detail Component', () => {
  let comp: CarteScolaireDetailComponent;
  let fixture: ComponentFixture<CarteScolaireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CarteScolaireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ carteScolaire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CarteScolaireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CarteScolaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load carteScolaire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.carteScolaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
