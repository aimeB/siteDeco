import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypeEvenementComponent } from './list/type-evenement.component';
import { TypeEvenementDetailComponent } from './detail/type-evenement-detail.component';
import { TypeEvenementUpdateComponent } from './update/type-evenement-update.component';
import { TypeEvenementDeleteDialogComponent } from './delete/type-evenement-delete-dialog.component';
import { TypeEvenementRoutingModule } from './route/type-evenement-routing.module';

@NgModule({
  imports: [SharedModule, TypeEvenementRoutingModule],
  declarations: [TypeEvenementComponent, TypeEvenementDetailComponent, TypeEvenementUpdateComponent, TypeEvenementDeleteDialogComponent],
  entryComponents: [TypeEvenementDeleteDialogComponent],
})
export class TypeEvenementModule {}
