import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { PresentationModule } from '@presentation/presentation.module';
import { ExtensionsModule } from '@extensions/extensions.module';
// routing
import { ReportesRoutingModule } from './reportes-routing.module';
import { CubeReportComponent } from './routes/cube-report/cube-report.component';
import { MonthlyReportComponent } from './routes/monthly-report/monthly-report.component';
// components
import { HomeComponent } from './routes/home/home.component';
import { InstitutionReportQueueComponent } from './components/monthly-queue/monthly-queue.component';
import { ReportTableComponent } from './components/report-table/report-table.component';

@NgModule({
	declarations: [
		// routing
		CubeReportComponent,
		MonthlyReportComponent,
		// components
		HomeComponent,
		InstitutionReportQueueComponent,
		ReportTableComponent,
	],
	imports: [
		CommonModule,
		// routing
		ReportesRoutingModule,
		// deps
		PresentationModule,
		ExtensionsModule,
	]
})
export class ReportesModule { }
