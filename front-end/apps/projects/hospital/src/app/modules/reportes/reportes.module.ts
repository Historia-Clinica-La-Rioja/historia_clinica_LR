import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { PresentationModule } from '@presentation/presentation.module';
import { ExtensionsModule } from '@extensions/extensions.module';
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
		ExtensionsModule
	]
})
export class ReportesModule { }
