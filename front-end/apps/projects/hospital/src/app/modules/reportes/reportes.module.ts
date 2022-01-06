import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { ReportesRoutingModule } from './reportes-routing.module';
import { HomeComponent } from './routes/home/home.component';

@NgModule({
	declarations: [
		// routing
		HomeComponent,
	],
	imports: [
		CommonModule,
		// routing
		ReportesRoutingModule,
		// deps
		PresentationModule,
	]
})
export class ReportesModule { }
