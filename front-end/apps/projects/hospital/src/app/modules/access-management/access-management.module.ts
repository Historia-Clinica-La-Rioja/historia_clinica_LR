import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccessManagementRoutingModule } from './access-management-routing.module';
//deps
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { SharedAppointmentAccessManagementModule } from '@shared-appointment-access-management/shared-appointment-access-management.module';
import { PresentationModule } from '@presentation/presentation.module';
//Standalone Components
import { AvailableAppointmentDataComponent } from '@turnos/standalone/components/available-appointment-data/available-appointment-data.component';
import { IdentifierCasesComponent } from '../hsi-components/identifier-cases/identifier-cases.component';
import { PatientSummaryComponent } from '../hsi-components/patient-summary/patient-summary.component';
import { ReferenceStateLabelComponent } from '../hsi-components/reference-state-label/reference-state-label.component';
import { ViewMedicalHistoryButtonComponent } from '../hsi-components/view-medical-history-button/view-medical-history-button.component';
import { ToAvailableAppointmentDataPipe } from '@turnos/standalone/pipes/to-available-appointment-data.pipe';
//components
import { ApprovalComponent } from './components/approval/approval.component';
import { ApprovalActionsComponent } from './components/approval-actions/approval-actions.component';
import { AppointmentSummaryComponent } from './components/appointment-summary/appointment-summary.component';
import { AvailableAppointmentCountInformationComponent } from './components/available-appointment-count-information/available-appointment-count-information.component';
import { ContactDetailsComponent } from './components/contact-details/contact-details.component';
import { DeriveRequestComponent } from './components/derive-request/derive-request.component';
import { DestinationInstitutionInformationComponent } from './components/destination-institution-information/destination-institution-information.component';
import { HomeComponent } from './routes/home/home.component';
import { HomeInstitutionInformationComponent } from './components/home-institution-information/home-institution-information.component';
import { InstitutionalActionsComponent } from './components/institutional-actions/institutional-actions.component';
import { InstitutionalNetworkActionsDropdownComponent } from './components/institutional-network-actions-dropdown/institutional-network-actions-dropdown.component';
import { ObservationsComponent } from './components/observations/observations.component';
import { PatientPhoneComponent } from './components/patient-phone/patient-phone.component';
import { PatientProblemsComponent } from './components/patient-problems/patient-problems.component';
import { PopupActionsComponent } from './components/popup-actions/popup-actions.component';
import { PriorizationCriteriaSelectComponent } from './components/priorization-criteria-select/priorization-criteria-select.component';
import { ReferenceCompleteDataComponent } from './components/reference-complete-data/reference-complete-data.component';
import { ReferenceFilesComponent } from './components/reference-files/reference-files.component';
import { ReferenceMedicalConceptsInformationComponent } from './components/reference-medical-concepts-information/reference-medical-concepts-information.component';
import { RegulationAppointmentResultViewComponent } from './components/regulation-appointment-result-view/regulation-appointment-result-view.component';
import { RegulationSearchCriteriaComponent } from './components/regulation-search-criteria/regulation-search-criteria.component';
import { SearchAppointmentsForRegulationComponent } from './components/search-appointments-for-regulation/search-appointments-for-regulation.component';
import { ShowPriorityComponent } from './components/show-priority/show-priority.component';
//dialogs
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
		AvailableAppointmentCountInformationComponent,
		ContactDetailsComponent,
		DeriveRequestComponent,
		DestinationInstitutionInformationComponent,
		InstitutionalActionsComponent,
		InstitutionalNetworkActionsDropdownComponent,
		HomeComponent,
		HomeInstitutionInformationComponent,
		ObservationsComponent,
		PatientPhoneComponent,
		PatientProblemsComponent,
		PopupActionsComponent,
		PriorizationCriteriaSelectComponent,
		ReferenceCompleteDataComponent,
		ReferenceFilesComponent,
		ReferenceMedicalConceptsInformationComponent,
		RegulationAppointmentResultViewComponent,
		RegulationSearchCriteriaComponent,
		SearchAppointmentsForRegulationComponent,
		ShowPriorityComponent,
		//dialogs
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
		//Standalone
		AvailableAppointmentDataComponent,
		IdentifierCasesComponent,
		PatientSummaryComponent,
		ReferenceStateLabelComponent,
		ToAvailableAppointmentDataPipe,
		ViewMedicalHistoryButtonComponent,
	],
	exports: [
		ReferenceCompleteDataComponent,
	]
})
export class AccessManagementModule { }
