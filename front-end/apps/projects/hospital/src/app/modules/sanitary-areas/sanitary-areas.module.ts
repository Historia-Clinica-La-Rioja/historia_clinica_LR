import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SanitaryAreasRoutingModule } from './sanitary-areas-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { PresentationModule } from '@presentation/presentation.module';


@NgModule({
	declarations: [
    	HomeComponent
  	],
	imports: [
		CommonModule,
		SanitaryAreasRoutingModule,
		PresentationModule
	]
})
export class SanitaryAreasModule { }
