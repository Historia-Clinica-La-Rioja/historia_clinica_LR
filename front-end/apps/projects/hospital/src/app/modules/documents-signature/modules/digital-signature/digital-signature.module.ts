import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
//deps
import { PresentationModule } from '@presentation/presentation.module';
//routes
import { DigitalSignatureRoutingModule } from './digital-signature-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { DocumentsSignatureModule } from '../../documents-signature.module';
@NgModule({
	declarations: [
		HomeComponent
	],
	imports: [
		CommonModule,
		DigitalSignatureRoutingModule,
		PresentationModule,
		DocumentsSignatureModule
	]
})
export class DigitalSignatureModule { }
