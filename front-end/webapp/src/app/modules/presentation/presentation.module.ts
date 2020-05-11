import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PatientCardComponent } from './components/patient-card/patient-card.component';
import { CoreModule } from '@core/core.module';
import { SummaryCardComponent } from './components/summary-card/summary-card.component';
import { InternmentEpisodeSummaryComponent } from './components/internment-episode-summary/internment-episode-summary.component';
import { NoDataComponent } from './components/no-data/no-data.component';
import { TableComponent } from './components/table/table.component';
import { SignoVitalCurrentPreviousComponent } from './components/signo-vital-current-previous/signo-vital-current-previous.component';
import { DetailBoxComponent } from './components/detail-box/detail-box.component';
import { FullHouseAddressPipe } from './pipes/fullHouseAddress.pipe';
import { PersonalInformationComponent } from './components/personal-information/personal-information.component';
import { PatientTypeLogoComponent } from './components/patient-type-logo/patient-type-logo.component';
import { MessageSnackbarComponent } from './components/message-snackbar/message-snackbar.component';

@NgModule({
	declarations: [
		InternmentEpisodeSummaryComponent,
		NoDataComponent,
		PatientCardComponent,
		SignoVitalCurrentPreviousComponent,
		SummaryCardComponent,
		TableComponent,
		DetailBoxComponent,
		InternmentEpisodeSummaryComponent,
		FullHouseAddressPipe,
		PersonalInformationComponent,
		PatientTypeLogoComponent,
		MessageSnackbarComponent,
	],
	imports: [
		CommonModule,
		CoreModule,
	],
	exports: [
		InternmentEpisodeSummaryComponent,
		NoDataComponent,
		PatientCardComponent,
		SignoVitalCurrentPreviousComponent,
		SummaryCardComponent,
		TableComponent,
		DetailBoxComponent,
		InternmentEpisodeSummaryComponent,
		FullHouseAddressPipe,
		PersonalInformationComponent,
		PatientTypeLogoComponent,
	]
})
export class PresentationModule { }
