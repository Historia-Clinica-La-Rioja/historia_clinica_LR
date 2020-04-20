import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from 'src/app/app.material.module';
import { PatientCardComponent } from './patient-card/patient-card.component';
import { CoreModule } from '@core/core.module';
import { SummaryCardComponent } from './summary-card/summary-card.component';
import { InternmentEpisodeSummaryComponent } from './internment-episode-summary/internment-episode-summary.component';


@NgModule({
	declarations: [
		PatientCardComponent,
		SummaryCardComponent,
		InternmentEpisodeSummaryComponent,
	],
	imports: [
		CommonModule,
		AppMaterialModule,
		CoreModule,
	],
	exports: [
		PatientCardComponent,
		SummaryCardComponent,
		InternmentEpisodeSummaryComponent,
	]
})
export class PresentationModule { }
