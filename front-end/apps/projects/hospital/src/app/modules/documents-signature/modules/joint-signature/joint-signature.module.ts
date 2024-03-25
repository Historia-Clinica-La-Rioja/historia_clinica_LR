import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './routes/home/home.component';
import { JointSignatureRoutingModule } from './joint-signature-routing.module';
import { PresentationModule } from '@presentation/presentation.module';


@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    JointSignatureRoutingModule,
    PresentationModule,
  ]
})
export class JointSignatureModule { }
