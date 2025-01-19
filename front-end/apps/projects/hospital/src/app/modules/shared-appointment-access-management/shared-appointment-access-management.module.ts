import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
//deps
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
//components
import { ModalityLabelComponent } from '@shared-appointment-access-management/components/modality-label/modality-label.component';
import { ModalityRadioButtonFormComponent } from '@shared-appointment-access-management/components/modality-radio-button-form/modality-radio-button-form.component';
import { ReferenceDashboardComponent } from './components/reference-dashboard/reference-dashboard.component';
import { ReferenceDashboardFiltersComponent } from './components/reference-dashboard-filters/reference-dashboard-filters.component';
import { ReferenceForwardingLabelComponent } from '@shared-appointment-access-management/components/reference-forwarding-label/reference-forwarding-label.component';
import { ReferenceReportComponent } from '@shared-appointment-access-management/components/reference-report/reference-report.component';
import { ReferenceSummaryComponent } from './components/reference-summary/reference-summary.component';
import { RegulationStateComponent } from '@shared-appointment-access-management/components/regulation-state/regulation-state.component';
import { TypeaheadPracticesComponent } from '@shared-appointment-access-management/components/typeahead-practices/typeahead-practices.component';
//Standalone Components
import { IdentifierCasesComponent } from '../hsi-components/identifier-cases/identifier-cases.component';
import { PatientSummaryComponent } from '../hsi-components/patient-summary/patient-summary.component';
import { ReferenceStateLabelComponent } from '../hsi-components/reference-state-label/reference-state-label.component';
//dialogs
import { ConfirmPrintAppointmentComponent } from '@shared-appointment-access-management/dialogs/confirm-print-appointment/confirm-print-appointment.component';
//pipes
import { ShowProblemsPipe } from '@shared-appointment-access-management/pipes/show-problems.pipe';



@NgModule({
  declarations: [
    //components
    ModalityRadioButtonFormComponent,
    ModalityLabelComponent,
    ReferenceDashboardComponent,
    ReferenceDashboardFiltersComponent,
    ReferenceForwardingLabelComponent,
    ReferenceReportComponent,
    ReferenceSummaryComponent,
    RegulationStateComponent,
    TypeaheadPracticesComponent,
    //dialogs
    ConfirmPrintAppointmentComponent,
    //pipes
		ShowProblemsPipe,
  ],
  imports: [
    CommonModule,
    //Standalone Components
    IdentifierCasesComponent,
    PatientSummaryComponent,
    ReferenceStateLabelComponent,
    //deps
    LazyMaterialModule,
    PresentationModule,
  ],
  exports: [
    //components
    ModalityRadioButtonFormComponent,
    ModalityLabelComponent,
    ReferenceDashboardComponent,
    ReferenceDashboardFiltersComponent,
    ReferenceReportComponent,
    ReferenceSummaryComponent,
    TypeaheadPracticesComponent,
    //dialogs
    ConfirmPrintAppointmentComponent,
    //pipes
		ShowProblemsPipe,
  ]
})
export class SharedAppointmentAccessManagementModule { }
