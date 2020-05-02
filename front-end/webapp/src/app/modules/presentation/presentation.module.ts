import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from 'src/app/app.material.module';
import { PatientCardComponent } from './components/patient-card/patient-card.component';
import { CoreModule } from '@core/core.module';
import { SummaryCardComponent } from './components/summary-card/summary-card.component';
import { InternmentEpisodeSummaryComponent } from './components/internment-episode-summary/internment-episode-summary.component';
import { NoDataComponent } from './components/no-data/no-data.component';
import { TableComponent } from './components/table/table.component';
import { SignoVitalCurrentPreviousComponent } from './components/signo-vital-current-previous/signo-vital-current-previous.component';

@NgModule({
	declarations: [
		InternmentEpisodeSummaryComponent,
		NoDataComponent,
		PatientCardComponent,
		SignoVitalCurrentPreviousComponent,
		SummaryCardComponent,
		TableComponent,
	],
	imports: [
		CommonModule,
		AppMaterialModule,
		CoreModule,
	],
	exports: [
		InternmentEpisodeSummaryComponent,
		NoDataComponent,
		PatientCardComponent,
		SignoVitalCurrentPreviousComponent,
		SummaryCardComponent,
		TableComponent,
	]
})
export class PresentationModule { }
