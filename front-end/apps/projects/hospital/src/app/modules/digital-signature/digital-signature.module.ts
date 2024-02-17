import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DigitalSignatureRoutingModule } from './digital-signature-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { PresentationModule } from '@presentation/presentation.module';


@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    DigitalSignatureRoutingModule,
    PresentationModule
  ]
})
export class DigitalSignatureModule { }
