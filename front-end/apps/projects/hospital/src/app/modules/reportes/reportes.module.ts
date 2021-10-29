import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';

import { ReportesRoutingModule } from './reportes-routing.module';

import { HomeComponent } from './routes/home/home.component';

@NgModule({
	declarations: [
		HomeComponent,
	],
	imports: [
		CommonModule,
		CoreModule,
		PresentationModule,
		ReportesRoutingModule,
	]
})
export class ReportesModule { }
