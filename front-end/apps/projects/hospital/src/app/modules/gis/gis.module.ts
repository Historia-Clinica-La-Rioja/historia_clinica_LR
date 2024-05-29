import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
//deps
import { PresentationModule } from '@presentation/presentation.module';
import { GisRoutingModule } from './gis-routing.module';
//components
import { HomeComponent } from './routes/home/home.component';


@NgModule({
	declarations: [
    	HomeComponent
  	],
	imports: [
		CommonModule,
		GisRoutingModule,
		PresentationModule
	]
})
export class GisModule { }
