import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITypeEvenement, TypeEvenement } from '../type-evenement.model';
import { TypeEvenementService } from '../service/type-evenement.service';

@Component({
  selector: 'jhi-type-evenement-update',
  templateUrl: './type-evenement-update.component.html',
})
export class TypeEvenementUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
  });

  constructor(protected typeEvenementService: TypeEvenementService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeEvenement }) => {
      this.updateForm(typeEvenement);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeEvenement = this.createFromForm();
    if (typeEvenement.id !== undefined) {
      this.subscribeToSaveResponse(this.typeEvenementService.update(typeEvenement));
    } else {
      this.subscribeToSaveResponse(this.typeEvenementService.create(typeEvenement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeEvenement>>): void {
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

  protected updateForm(typeEvenement: ITypeEvenement): void {
    this.editForm.patchValue({
      id: typeEvenement.id,
      nom: typeEvenement.nom,
    });
  }

  protected createFromForm(): ITypeEvenement {
    return {
      ...new TypeEvenement(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }
}
