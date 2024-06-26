import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DocumentsSignatureRoutingModule } from './documents-signature-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { PresentationModule } from '@presentation/presentation.module';
import { SummaryMultipleSignComponent } from './components/summary-multiple-sign/summary-multiple-sign.component';
import { SummaryListMultipleSignComponent } from './components/summary-list-multiple-sign/summary-list-multiple-sign.component';
import { SummaryCardMultipleSignComponent } from './components/summary-card-multiple-sign/summary-card-multiple-sign.component';
import { IdentifierCasesComponent } from '@hsi-components/identifier-cases/identifier-cases.component';
import { SignStatusComponent } from '@hsi-components/sign-status/sign-status.component';


@NgModule({
	declarations: [
		HomeComponent,
		SummaryMultipleSignComponent,
		SummaryListMultipleSignComponent,
		SummaryCardMultipleSignComponent
	],
	imports: [
		CommonModule,
		DocumentsSignatureRoutingModule,
		PresentationModule,
		IdentifierCasesComponent,
		SignStatusComponent
	],
	exports: [
		SummaryCardMultipleSignComponent
	]
})
export class DocumentsSignatureModule { }
