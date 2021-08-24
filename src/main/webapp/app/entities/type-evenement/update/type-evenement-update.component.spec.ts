jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TypeEvenementService } from '../service/type-evenement.service';
import { ITypeEvenement, TypeEvenement } from '../type-evenement.model';

import { TypeEvenementUpdateComponent } from './type-evenement-update.component';

describe('Component Tests', () => {
  describe('TypeEvenement Management Update Component', () => {
    let comp: TypeEvenementUpdateComponent;
    let fixture: ComponentFixture<TypeEvenementUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let typeEvenementService: TypeEvenementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TypeEvenementUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TypeEvenementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeEvenementUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      typeEvenementService = TestBed.inject(TypeEvenementService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const typeEvenement: ITypeEvenement = { id: 456 };

        activatedRoute.data = of({ typeEvenement });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(typeEvenement));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TypeEvenement>>();
        const typeEvenement = { id: 123 };
        jest.spyOn(typeEvenementService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeEvenement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeEvenement }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(typeEvenementService.update).toHaveBeenCalledWith(typeEvenement);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TypeEvenement>>();
        const typeEvenement = new TypeEvenement();
        jest.spyOn(typeEvenementService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeEvenement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: typeEvenement }));
        saveSubject.complete();

        // THEN
        expect(typeEvenementService.create).toHaveBeenCalledWith(typeEvenement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TypeEvenement>>();
        const typeEvenement = { id: 123 };
        jest.spyOn(typeEvenementService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ typeEvenement });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(typeEvenementService.update).toHaveBeenCalledWith(typeEvenement);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
