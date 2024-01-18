import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccessManagementRoutingModule } from './access-management-routing.module';
//deps
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { SharedAppointmentAccessManagementModule } from '@shared-appointment-access-management/shared-appointment-access-management.module';
import { PresentationModule } from '@presentation/presentation.module';
//Standalone Components
import { IdentifierCasesComponent } from '../hsi-components/identifier-cases/identifier-cases.component';
import { PatientSummaryComponent } from '../hsi-components/patient-summary/patient-summary.component';
import { ReferenceStateLabelComponent } from '../hsi-components/reference-state-label/reference-state-label.component';
import { ViewMedicalHistoryButtonComponent } from '../hsi-components/view-medical-history-button/view-medical-history-button.component';
//components
import { ApprovalComponent } from './components/approval/approval.component';
import { ApprovalActionsComponent } from './components/approval-actions/approval-actions.component';
import { AppointmentSummaryComponent } from './components/appointment-summary/appointment-summary.component';
import { ContactDetailsComponent } from './components/contact-details/contact-details.component';
import { DeriveRequestComponent } from './components/derive-request/derive-request.component';
import { HomeComponent } from './routes/home/home.component';
import { InstitutionalActionsComponent } from './components/institutional-actions/institutional-actions.component';
import { InstitutionalNetworkActionsComponent } from './components/institutional-network-actions/institutional-network-actions.component';
import { PopupActionsComponent } from './components/popup-actions/popup-actions.component';
import { ReferenceCompleteDataComponent } from './components/reference-complete-data/reference-complete-data.component';
import { RegulationAppointmentResultViewComponent } from './components/regulation-appointment-result-view/regulation-appointment-result-view.component';
import { RegulationSearchCriteriaComponent } from './components/regulation-search-criteria/regulation-search-criteria.component';
import { SearchAppointmentsForRegulationComponent } from './components/search-appointments-for-regulation/search-appointments-for-regulation.component';
//dialogs
import { ReasonPopUpComponent } from './dialogs/reason-pop-up/reason-pop-up.component';
import { RegulationNewAppointmentPopUpComponent } from './dialogs/regulation-new-appointment-pop-up/regulation-new-appointment-pop-up.component';
import { ReferenceEditionPopUpComponent } from './dialogs/reference-edition-pop-up/reference-edition-pop-up.component';
import { ReportCompleteDataPopupComponent } from './dialogs/report-complete-data-popup/report-complete-data-popup.component';
//services
import { TabsService } from './services/tabs.service';


@NgModule({
	declarations: [
		//components
		ApprovalComponent,
		ApprovalActionsComponent,
		AppointmentSummaryComponent,
		ContactDetailsComponent,
		DeriveRequestComponent,
		InstitutionalActionsComponent,
		InstitutionalNetworkActionsComponent,
		HomeComponent,
		PopupActionsComponent,
		ReferenceCompleteDataComponent,
		RegulationAppointmentResultViewComponent,
		RegulationSearchCriteriaComponent,
		SearchAppointmentsForRegulationComponent,
		//dialogs
		ReasonPopUpComponent,
		ReferenceEditionPopUpComponent,
		RegulationNewAppointmentPopUpComponent,
		ReportCompleteDataPopupComponent,
	],
	providers: [
		TabsService
	],
	imports: [
		AccessManagementRoutingModule,
		CommonModule,
		//deps
		LazyMaterialModule,
		PresentationModule,
		SharedAppointmentAccessManagementModule,
		//Standalone Components
		IdentifierCasesComponent,
		PatientSummaryComponent,
		ReferenceStateLabelComponent,
		ViewMedicalHistoryButtonComponent,
	],
	exports: [
		ReferenceCompleteDataComponent,
	]
})
export class AccessManagementModule { }
