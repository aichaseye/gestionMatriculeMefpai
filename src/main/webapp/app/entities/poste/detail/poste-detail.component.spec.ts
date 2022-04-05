import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PosteDetailComponent } from './poste-detail.component';

describe('Poste Management Detail Component', () => {
  let comp: PosteDetailComponent;
  let fixture: ComponentFixture<PosteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PosteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ poste: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PosteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PosteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load poste on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.poste).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
