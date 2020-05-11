import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreModule } from '@core/core.module';

import { InstitucionRoutingModule } from './institucion-routing.module';
import { InstitucionComponent } from './institucion.component';

import { HomeComponent } from './routes/home/home.component';


@NgModule({
	declarations: [
		InstitucionComponent,
		HomeComponent,
	],
	imports: [
		CommonModule,
		CoreModule,
		InstitucionRoutingModule,
	]
})
export class InstitucionModule { }
