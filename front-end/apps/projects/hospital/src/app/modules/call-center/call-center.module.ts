import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CallCenterRoutingModule } from '@call-center/call-center-routing.module';
//components
import { HomeComponent } from '@call-center/routes/home/home.component';
import { SearchAppointmentsForThirdPartyComponent } from '@call-center/components/search-appointments-for-third-party/search-appointments-for-third-party.component';
//deps
import { PresentationModule } from '@presentation/presentation.module';


@NgModule({
	declarations: [
		//components
		HomeComponent,
		SearchAppointmentsForThirdPartyComponent,
	],
	imports: [
		CommonModule,
		CallCenterRoutingModule,
		//deps
		PresentationModule
	]
})
export class CallCenterModule { }
