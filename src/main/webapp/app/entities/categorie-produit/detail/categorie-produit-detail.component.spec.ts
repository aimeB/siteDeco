import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategorieProduitDetailComponent } from './categorie-produit-detail.component';

describe('Component Tests', () => {
  describe('CategorieProduit Management Detail Component', () => {
    let comp: CategorieProduitDetailComponent;
    let fixture: ComponentFixture<CategorieProduitDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CategorieProduitDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ categorieProduit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CategorieProduitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CategorieProduitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load categorieProduit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.categorieProduit).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
