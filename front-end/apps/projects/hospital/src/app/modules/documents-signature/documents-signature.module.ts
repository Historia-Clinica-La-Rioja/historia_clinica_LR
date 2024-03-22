import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DocumentsSignatureRoutingModule } from './documents-signature-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { PresentationModule } from '@presentation/presentation.module';


@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    DocumentsSignatureRoutingModule,
    PresentationModule,
  ]
})
export class DocumentsSignatureModule { }
