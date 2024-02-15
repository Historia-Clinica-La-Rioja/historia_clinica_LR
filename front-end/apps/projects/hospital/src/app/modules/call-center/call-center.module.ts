import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CallCenterRoutingModule } from '@call-center/call-center-routing.module';
//components
import { AvailableAppointmentListComponent } from '@call-center/components/available-appointment-list/available-appointment-list.component';
import { EmailCustomFormComponent } from './components/email-custom-form/email-custom-form.component';
import { FullNameCustomFormComponent } from './components/full-name-custom-form/full-name-custom-form.component';
import { GenderCustomFormComponent } from './components/gender-custom-form/gender-custom-form.component';
import { HomeComponent } from '@call-center/routes/home/home.component';
import { IdentificationNumberCustomFormComponent } from './components/identification-number-custom-form/identification-number-custom-form.component';
import { NewAppointmentForThirdPartyPopupComponent } from './dialogs/new-appointment-for-third-party-popup/new-appointment-for-third-party-popup.component';
import { PhoneCustomFormComponent } from './components/phone-custom-form/phone-custom-form.component';
import { SearchAppointmentsForThirdPartyComponent } from '@call-center/components/search-appointments-for-third-party/search-appointments-for-third-party.component';
//deps
import { PresentationModule } from '@presentation/presentation.module';
//deps standalone
import { AvailableAppointmentDataComponent } from '@turnos/standalone/components/available-appointment-data/available-appointment-data.component';
import { ToAvailableAppointmentDataPipe } from '@turnos/standalone/pipes/to-available-appointment-data.pipe';

@NgModule({
	declarations: [
		//components
		AvailableAppointmentListComponent,
		EmailCustomFormComponent,
		FullNameCustomFormComponent,
		GenderCustomFormComponent,
		HomeComponent,
		IdentificationNumberCustomFormComponent,
		NewAppointmentForThirdPartyPopupComponent,
		PhoneCustomFormComponent,
		SearchAppointmentsForThirdPartyComponent,
	],
	imports: [
		CommonModule,
		CallCenterRoutingModule,
		//deps
		PresentationModule,
		//Standalone
		AvailableAppointmentDataComponent,
		ToAvailableAppointmentDataPipe,
	]
})
export class CallCenterModule { }
