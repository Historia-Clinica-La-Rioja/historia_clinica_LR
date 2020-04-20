import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from 'src/app/app.material.module';
import { PatientCardComponent } from './patient-card/patient-card.component';
import { CoreModule } from '@core/core.module';
import { SummaryCardComponent } from './summary-card/summary-card.component';


@NgModule({
	declarations: [
		PatientCardComponent,
		SummaryCardComponent,
	],
	imports: [
		CommonModule,
		AppMaterialModule,
		CoreModule,
	],
	exports: [
		PatientCardComponent,
		SummaryCardComponent,
	]
})
export class PresentationModule { }
