import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccessManagementRoutingModule } from './access-management-routing.module';
//deps
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
import { SharedAppointmentAccessManagementModule } from '@shared-appointment-access-management/shared-appointment-access-management.module';
//Standalone Components
import { IdentifierCasesComponent } from '../hsi-components/identifier-cases/identifier-cases.component';
import { PatientSummaryComponent } from '../hsi-components/patient-summary/patient-summary.component';
import { ReferenceStateLabelComponent } from '../hsi-components/reference-state-label/reference-state-label.component';
import { ViewMedicalHistoryButtonComponent } from '../hsi-components/view-medical-history-button/view-medical-history-button.component';
//components
import { ApprovalComponent } from './components/approval/approval.component';
import { AppointmentSummaryComponent } from './components/appointment-summary/appointment-summary.component';
import { ContactDetailsComponent } from './components/contact-details/contact-details.component';
import { HomeComponent } from './routes/home/home.component';
import { InstitutionalActionsComponent } from './components/institutional-actions/institutional-actions.component';
import { InstitutionalNetworkActionsComponent } from './components/institutional-network-actions/institutional-network-actions.component';
import { PopupActionsComponent } from './components/popup-actions/popup-actions.component';
import { ReferenceCompleteDataComponent } from './components/reference-complete-data/reference-complete-data.component';
//dialogs
import { ReasonPopUpComponent } from './dialogs/reason-pop-up/reason-pop-up.component';
import { ReportCompleteDataPopupComponent } from './dialogs/report-complete-data-popup/report-complete-data-popup.component';

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
		//dialogs
		ReasonPopUpComponent,
		ReportCompleteDataPopupComponent,
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
