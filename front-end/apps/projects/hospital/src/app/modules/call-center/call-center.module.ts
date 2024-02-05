import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CallCenterRoutingModule } from '@call-center/call-center-routing.module';
//components
import { AvailableAppointmentListComponent } from '@call-center/components/available-appointment-list/available-appointment-list.component';
import { HomeComponent } from '@call-center/routes/home/home.component';
import { SearchAppointmentsForThirdPartyComponent } from '@call-center/components/search-appointments-for-third-party/search-appointments-for-third-party.component';
//deps
import { PresentationModule } from '@presentation/presentation.module';
//deps standalone
import { AvailableAppointmentDataComponent } from '@turnos/components-standalone/available-appointment-data/available-appointment-data.component';

@NgModule({
	declarations: [
		//components
		AvailableAppointmentListComponent,
		HomeComponent,
		SearchAppointmentsForThirdPartyComponent,
	],
	imports: [
		CommonModule,
		CallCenterRoutingModule,
		//deps
		PresentationModule,
		//Standalone
		AvailableAppointmentDataComponent
	]
})
export class CallCenterModule { }
