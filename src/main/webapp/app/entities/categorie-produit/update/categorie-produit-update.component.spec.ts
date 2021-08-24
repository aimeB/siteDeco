jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CategorieProduitService } from '../service/categorie-produit.service';
import { ICategorieProduit, CategorieProduit } from '../categorie-produit.model';

import { CategorieProduitUpdateComponent } from './categorie-produit-update.component';

describe('Component Tests', () => {
  describe('CategorieProduit Management Update Component', () => {
    let comp: CategorieProduitUpdateComponent;
    let fixture: ComponentFixture<CategorieProduitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let categorieProduitService: CategorieProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CategorieProduitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CategorieProduitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CategorieProduitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      categorieProduitService = TestBed.inject(CategorieProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const categorieProduit: ICategorieProduit = { id: 456 };

        activatedRoute.data = of({ categorieProduit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(categorieProduit));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CategorieProduit>>();
        const categorieProduit = { id: 123 };
        jest.spyOn(categorieProduitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ categorieProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: categorieProduit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(categorieProduitService.update).toHaveBeenCalledWith(categorieProduit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CategorieProduit>>();
        const categorieProduit = new CategorieProduit();
        jest.spyOn(categorieProduitService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ categorieProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: categorieProduit }));
        saveSubject.complete();

        // THEN
        expect(categorieProduitService.create).toHaveBeenCalledWith(categorieProduit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CategorieProduit>>();
        const categorieProduit = { id: 123 };
        jest.spyOn(categorieProduitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ categorieProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(categorieProduitService.update).toHaveBeenCalledWith(categorieProduit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
