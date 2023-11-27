import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
//deps
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
import { IdentifierCasesComponent } from '../hsi-components/identifier-cases/identifier-cases.component';
import { PatientSummaryComponent } from '../hsi-components/patient-summary/patient-summary.component';
//components
import { ApprovalComponent } from './components/approval/approval.component';
import { AppointmentSummaryComponent } from './components/appointment-summary/appointment-summary.component';
import { ContactDetailsComponent } from './components/contact-details/contact-details.component';
import { HomeComponent } from './routes/home/home.component';
import { InstitutionalActionsComponent } from './components/institutional-actions/institutional-actions.component';
import { InstitutionalNetworkActionsComponent } from './components/institutional-network-actions/institutional-network-actions.component';
import { PopupActionsComponent } from './components/popup-actions/popup-actions.component';
import { ReferenceCompleteDataComponent } from './components/reference-complete-data/reference-complete-data.component';
import { ReferenceDashboardComponent } from './components/reference-dashboard/reference-dashboard.component';
import { ReferenceDashboardFiltersComponent } from './components/reference-dashboard-filters/reference-dashboard-filters.component';
import { ReferenceReportComponent } from './components/reference-report/reference-report.component';
import { ReferenceSummaryComponent } from './components/reference-summary/reference-summary.component';
//dialogs
import { ReasonPopUpComponent } from './dialogs/reason-pop-up/reason-pop-up.component';
import { ReportCompleteDataPopupComponent } from './dialogs/report-complete-data-popup/report-complete-data-popup.component';
//pipes
import { ShowProblemsPipe } from './pipes/show-problems.pipe';
import { AccessManagementRoutingModule } from './access-management-routing.module';
import { HistoriaClinicaModule } from '@historia-clinica/historia-clinica.module';

@NgModule({
	declarations: [
		//components
		ApprovalComponent,
		AppointmentSummaryComponent,
		ContactDetailsComponent,
		InstitutionalActionsComponent,
		InstitutionalNetworkActionsComponent,
		HomeComponent,
		PopupActionsComponent,
		ReferenceCompleteDataComponent,
		ReferenceDashboardComponent,
		ReferenceReportComponent,
		ReferenceDashboardFiltersComponent,
		ReferenceSummaryComponent,
		//dialogs
		ReasonPopUpComponent,
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
		HistoriaClinicaModule,
	],
	exports: [
		ReferenceCompleteDataComponent,
		ReferenceReportComponent,
	]
})
export class AccessManagementModule { }
