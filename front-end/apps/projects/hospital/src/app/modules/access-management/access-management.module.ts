import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
//deps
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
import { IdentifierCasesComponent } from '../hsi-components/identifier-cases/identifier-cases.component';
import { PatientSummaryComponent } from '../hsi-components/patient-summary/patient-summary.component';
//components
import { AppointmentSummaryComponent } from './components/appointment-summary/appointment-summary.component';
import { ContactDetailsComponent } from './components/contact-details/contact-details.component';
import { HomeComponent } from './routes/home/home.component';
import { ReferenceCompleteDataComponent } from './components/reference-complete-data/reference-complete-data.component';
import { ReferenceDashboardComponent } from './components/reference-dashboard/reference-dashboard.component';
import { ReferenceDashboardFiltersComponent } from './components/reference-dashboard-filters/reference-dashboard-filters.component';
import { ReferenceReportComponent } from './components/reference-report/reference-report.component';
import { ReferenceSummaryComponent } from './components/reference-summary/reference-summary.component';
//dialogs
import { ReportCompleteDataPopupComponent } from './dialogs/report-complete-data-popup/report-complete-data-popup.component';
//pipes
import { ShowProblemsPipe } from './pipes/show-problems.pipe';
import { AccessManagementRoutingModule } from './access-management-routing.module';

@NgModule({
	declarations: [
		//components
		AppointmentSummaryComponent,
		ContactDetailsComponent,
		HomeComponent,
		ReferenceCompleteDataComponent,
		ReferenceDashboardComponent,
		ReferenceReportComponent,
		ReferenceDashboardFiltersComponent,
		ReferenceSummaryComponent,
		//dialogs
		ReportCompleteDataPopupComponent,
		//pipes
		ShowProblemsPipe,
	],
	imports: [
		AccessManagementRoutingModule,
		CommonModule,
		IdentifierCasesComponent,
		LazyMaterialModule,
		PatientSummaryComponent,
		PresentationModule,
	],
	exports: [
		ReferenceCompleteDataComponent,
		ReferenceReportComponent,
	]
})
export class AccessManagementModule { }
