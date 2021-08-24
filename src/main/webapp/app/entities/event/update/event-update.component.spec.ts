jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EventService } from '../service/event.service';
import { IEvent, Event } from '../event.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITypeEvenement } from 'app/entities/type-evenement/type-evenement.model';
import { TypeEvenementService } from 'app/entities/type-evenement/service/type-evenement.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { ISalle } from 'app/entities/salle/salle.model';
import { SalleService } from 'app/entities/salle/service/salle.service';

import { EventUpdateComponent } from './event-update.component';

describe('Component Tests', () => {
  describe('Event Management Update Component', () => {
    let comp: EventUpdateComponent;
    let fixture: ComponentFixture<EventUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let eventService: EventService;
    let userService: UserService;
    let typeEvenementService: TypeEvenementService;
    let produitService: ProduitService;
    let salleService: SalleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EventUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EventUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      eventService = TestBed.inject(EventService);
      userService = TestBed.inject(UserService);
      typeEvenementService = TestBed.inject(TypeEvenementService);
      produitService = TestBed.inject(ProduitService);
      salleService = TestBed.inject(SalleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const event: IEvent = { id: 456 };
        const appartenantA: IUser = { id: 53058 };
        event.appartenantA = appartenantA;
        const agentEvenements: IUser[] = [{ id: 23522 }];
        event.agentEvenements = agentEvenements;

        const userCollection: IUser[] = [{ id: 79014 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [appartenantA, ...agentEvenements];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call TypeEvenement query and add missing value', () => {
        const event: IEvent = { id: 456 };
        const typeEvenement: ITypeEvenement = { id: 25178 };
        event.typeEvenement = typeEvenement;

        const typeEvenementCollection: ITypeEvenement[] = [{ id: 41544 }];
        jest.spyOn(typeEvenementService, 'query').mockReturnValue(of(new HttpResponse({ body: typeEvenementCollection })));
        const additionalTypeEvenements = [typeEvenement];
        const expectedCollection: ITypeEvenement[] = [...additionalTypeEvenements, ...typeEvenementCollection];
        jest.spyOn(typeEvenementService, 'addTypeEvenementToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(typeEvenementService.query).toHaveBeenCalled();
        expect(typeEvenementService.addTypeEvenementToCollectionIfMissing).toHaveBeenCalledWith(
          typeEvenementCollection,
          ...additionalTypeEvenements
        );
        expect(comp.typeEvenementsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Produit query and add missing value', () => {
        const event: IEvent = { id: 456 };
        const produits: IProduit[] = [{ id: 72318 }];
        event.produits = produits;

        const produitCollection: IProduit[] = [{ id: 47292 }];
        jest.spyOn(produitService, 'query').mockReturnValue(of(new HttpResponse({ body: produitCollection })));
        const additionalProduits = [...produits];
        const expectedCollection: IProduit[] = [...additionalProduits, ...produitCollection];
        jest.spyOn(produitService, 'addProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(produitService.query).toHaveBeenCalled();
        expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(produitCollection, ...additionalProduits);
        expect(comp.produitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Salle query and add missing value', () => {
        const event: IEvent = { id: 456 };
        const salle: ISalle = { id: 78221 };
        event.salle = salle;

        const salleCollection: ISalle[] = [{ id: 2577 }];
        jest.spyOn(salleService, 'query').mockReturnValue(of(new HttpResponse({ body: salleCollection })));
        const additionalSalles = [salle];
        const expectedCollection: ISalle[] = [...additionalSalles, ...salleCollection];
        jest.spyOn(salleService, 'addSalleToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(salleService.query).toHaveBeenCalled();
        expect(salleService.addSalleToCollectionIfMissing).toHaveBeenCalledWith(salleCollection, ...additionalSalles);
        expect(comp.sallesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const event: IEvent = { id: 456 };
        const appartenantA: IUser = { id: 79249 };
        event.appartenantA = appartenantA;
        const agentEvenements: IUser = { id: 43969 };
        event.agentEvenements = [agentEvenements];
        const typeEvenement: ITypeEvenement = { id: 77156 };
        event.typeEvenement = typeEvenement;
        const produits: IProduit = { id: 80261 };
        event.produits = [produits];
        const salle: ISalle = { id: 89359 };
        event.salle = salle;

        activatedRoute.data = of({ event });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(event));
        expect(comp.usersSharedCollection).toContain(appartenantA);
        expect(comp.usersSharedCollection).toContain(agentEvenements);
        expect(comp.typeEvenementsSharedCollection).toContain(typeEvenement);
        expect(comp.produitsSharedCollection).toContain(produits);
        expect(comp.sallesSharedCollection).toContain(salle);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Event>>();
        const event = { id: 123 };
        jest.spyOn(eventService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: event }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(eventService.update).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Event>>();
        const event = new Event();
        jest.spyOn(eventService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: event }));
        saveSubject.complete();

        // THEN
        expect(eventService.create).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Event>>();
        const event = { id: 123 };
        jest.spyOn(eventService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ event });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(eventService.update).toHaveBeenCalledWith(event);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTypeEvenementById', () => {
        it('Should return tracked TypeEvenement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTypeEvenementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackProduitById', () => {
        it('Should return tracked Produit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProduitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSalleById', () => {
        it('Should return tracked Salle primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSalleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedUser', () => {
        it('Should return option if no User is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedUser(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected User for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedUser(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this User is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedUser(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedProduit', () => {
        it('Should return option if no Produit is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedProduit(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Produit for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedProduit(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Produit is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedProduit(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
