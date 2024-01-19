import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CallCenterRoutingModule } from '@call-center/call-center-routing.module';
//components
import { HomeComponent } from '@call-center/routes/home/home.component';


@NgModule({
	declarations: [
		//components
		HomeComponent,
	],
	imports: [
		CommonModule,
		CallCenterRoutingModule,
	]
})
export class CallCenterModule { }
