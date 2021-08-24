import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEvent, Event } from '../event.model';
import { EventService } from '../service/event.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITypeEvenement } from 'app/entities/type-evenement/type-evenement.model';
import { TypeEvenementService } from 'app/entities/type-evenement/service/type-evenement.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';
import { ISalle } from 'app/entities/salle/salle.model';
import { SalleService } from 'app/entities/salle/service/salle.service';

@Component({
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html',
})
export class EventUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  typeEvenementsSharedCollection: ITypeEvenement[] = [];
  produitsSharedCollection: IProduit[] = [];
  sallesSharedCollection: ISalle[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    dateEvenement: [null, [Validators.required]],
    prix: [],
    appartenantA: [],
    agentEvenements: [],
    typeEvenement: [null, Validators.required],
    produits: [],
    salle: [null, Validators.required],
  });

  constructor(
    protected eventService: EventService,
    protected userService: UserService,
    protected typeEvenementService: TypeEvenementService,
    protected produitService: ProduitService,
    protected salleService: SalleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      if (event.id === undefined) {
        const today = dayjs().startOf('day');
        event.dateEvenement = today;
      }

      this.updateForm(event);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const event = this.createFromForm();
    if (event.id !== undefined) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackTypeEvenementById(index: number, item: ITypeEvenement): number {
    return item.id!;
  }

  trackProduitById(index: number, item: IProduit): number {
    return item.id!;
  }

  trackSalleById(index: number, item: ISalle): number {
    return item.id!;
  }

  getSelectedUser(option: IUser, selectedVals?: IUser[]): IUser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedProduit(option: IProduit, selectedVals?: IProduit[]): IProduit {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(event: IEvent): void {
    this.editForm.patchValue({
      id: event.id,
      nom: event.nom,
      dateEvenement: event.dateEvenement ? event.dateEvenement.format(DATE_TIME_FORMAT) : null,
      prix: event.prix,
      appartenantA: event.appartenantA,
      agentEvenements: event.agentEvenements,
      typeEvenement: event.typeEvenement,
      produits: event.produits,
      salle: event.salle,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      event.appartenantA,
      ...(event.agentEvenements ?? [])
    );
    this.typeEvenementsSharedCollection = this.typeEvenementService.addTypeEvenementToCollectionIfMissing(
      this.typeEvenementsSharedCollection,
      event.typeEvenement
    );
    this.produitsSharedCollection = this.produitService.addProduitToCollectionIfMissing(
      this.produitsSharedCollection,
      ...(event.produits ?? [])
    );
    this.sallesSharedCollection = this.salleService.addSalleToCollectionIfMissing(this.sallesSharedCollection, event.salle);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(
            users,
            this.editForm.get('appartenantA')!.value,
            ...(this.editForm.get('agentEvenements')!.value ?? [])
          )
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.typeEvenementService
      .query()
      .pipe(map((res: HttpResponse<ITypeEvenement[]>) => res.body ?? []))
      .pipe(
        map((typeEvenements: ITypeEvenement[]) =>
          this.typeEvenementService.addTypeEvenementToCollectionIfMissing(typeEvenements, this.editForm.get('typeEvenement')!.value)
        )
      )
      .subscribe((typeEvenements: ITypeEvenement[]) => (this.typeEvenementsSharedCollection = typeEvenements));

    this.produitService
      .query()
      .pipe(map((res: HttpResponse<IProduit[]>) => res.body ?? []))
      .pipe(
        map((produits: IProduit[]) =>
          this.produitService.addProduitToCollectionIfMissing(produits, ...(this.editForm.get('produits')!.value ?? []))
        )
      )
      .subscribe((produits: IProduit[]) => (this.produitsSharedCollection = produits));

    this.salleService
      .query()
      .pipe(map((res: HttpResponse<ISalle[]>) => res.body ?? []))
      .pipe(map((salles: ISalle[]) => this.salleService.addSalleToCollectionIfMissing(salles, this.editForm.get('salle')!.value)))
      .subscribe((salles: ISalle[]) => (this.sallesSharedCollection = salles));
  }

  protected createFromForm(): IEvent {
    return {
      ...new Event(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      dateEvenement: this.editForm.get(['dateEvenement'])!.value
        ? dayjs(this.editForm.get(['dateEvenement'])!.value, DATE_TIME_FORMAT)
        : undefined,
      prix: this.editForm.get(['prix'])!.value,
      appartenantA: this.editForm.get(['appartenantA'])!.value,
      agentEvenements: this.editForm.get(['agentEvenements'])!.value,
      typeEvenement: this.editForm.get(['typeEvenement'])!.value,
      produits: this.editForm.get(['produits'])!.value,
      salle: this.editForm.get(['salle'])!.value,
    };
  }
}
