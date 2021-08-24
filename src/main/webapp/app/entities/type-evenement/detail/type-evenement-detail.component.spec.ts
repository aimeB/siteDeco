import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TypeEvenementDetailComponent } from './type-evenement-detail.component';

describe('Component Tests', () => {
  describe('TypeEvenement Management Detail Component', () => {
    let comp: TypeEvenementDetailComponent;
    let fixture: ComponentFixture<TypeEvenementDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TypeEvenementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ typeEvenement: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TypeEvenementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeEvenementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load typeEvenement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeEvenement).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
