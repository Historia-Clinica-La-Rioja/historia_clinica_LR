import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// deps
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
import { InternacionesModule } from '@historia-clinica/modules/ambulatoria/modules/internacion/internaciones.module';
import { GuardiaModule } from '@historia-clinica/modules/guardia/guardia.module';

// routing
import { PacientesRoutingModule } from './pacientes-routing.module';

import { EditPatientComponent } from './routes/edit-patient/edit-patient.component';
import { EmergencyCareTemporaryPatientProfile } from './routes/emergency-care-temporary-patient-profile/emergency-care-temporary-patient-profile';
import { HomeComponent } from './routes/home/home.component';
import { NewPatientComponent } from './routes/new-patient/new-patient.component';
import { NewTemporaryPatientComponent } from './routes/new-temporary-patient/new-temporary-patient.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { SearchComponent } from './routes/search/search.component';
// components
import { AppointmentHistoricDetailComponent } from './component/appointment-historic-detail/appointment-historic-detail.component';
import { AssignedAppointmentComponent } from './component/assigned-appointment/assigned-appointment.component';
import { AssignedAppointmentsComponent } from './component/assigned-appointments/assigned-appointments.component';
import { BookingAppointmentsComponent } from './component/booking-appointments/booking-appointments.component';
import { CardLicenseComponent } from './component/card-license/card-license.component';
import { CardPatientComponent } from './component/card-patient/card-patient.component';
import { CardProfessionsComponent } from './component/card-professions/card-professions.component';
import { CardRolesComponent } from './component/card-roles/card-roles.component';
import { EditIdentificationNumberComponent } from './dialogs/edit-identification-number/edit-identification-number.component';
import { FilesUploaderComponent } from './component/files-uploader/files-uploader.component';
import { HierarchicalUnitsComponent } from './component/hierarchical-units/hierarchical-units.component';
import { LicenseFormComponent } from './component/license-form/license-form.component';
import { MedicalDischargeSummaryComponent } from './component/medical-discharge-summary/medical-discharge-summary.component';
import { ProfessionSpecialtiesFormComponent } from './component/profession-specialties-form/profession-specialties-form.component';
import { ResumenDeGuardiaComponent } from './component/resumen-de-guardia/resumen-de-guardia.component';
import { SearchCreateComponent } from './component/search-create/search-create.component';
import { SearchPatientComponent } from './component/search-patient/search-patient.component';
import { ViewPatientDetailComponent } from './component/view-patient-detail/view-patient-detail.component';
//Standalone component
import { EmergencyCareStatusLabelsComponent } from '@hsi-components/emergency-care-status-labels/emergency-care-status-labels.component';
import { IdentifierCasesComponent } from '../hsi-components/identifier-cases/identifier-cases.component';
import { ReferenceStateLabelComponent } from '../hsi-components/reference-state-label/reference-state-label.component';
// dialogs
import { AppointmentHistoricComponent } from './dialogs/appointment-historic/appointment-historic.component';
import { ArtComponent } from './dialogs/art/art.component';
import { EditHierarchicalUnitsComponent } from './dialogs/edit-hierarchical-units/edit-hierarchical-units.component';
import { EditLicenseComponent } from './dialogs/edit-license/edit-license.component';
import { EditPrefessionsSpecialtiesComponent } from './dialogs/edit-prefessions-specialties/edit-prefessions-specialties.component';
import { EditRolesComponent } from './dialogs/edit-roles/edit-roles.component';
import { HealthInsuranceComponent } from './dialogs/health-insurance/health-insurance.component';
import { MedicalCoverageComponent } from "./dialogs/medical-coverage/medical-coverage.component";
import { MessageForAuditComponent } from './dialogs/message-for-audit/message-for-audit.component';
import { PrivateHealthInsuranceComponent } from './dialogs/private-health-insurance/private-health-insurance.component';
import { ReportsComponent } from './dialogs/reports/reports.component';
import { ScanPatientComponent } from './dialogs/scan-patient/scan-patient.component';
import { SearchPatientDialogComponent } from './dialogs/search-patient-dialog/search-patient-dialog.component';
import { WarningEditIdentificationNumberComponent } from './dialogs/warning-edit-identification-number/warning-edit-identification-number.component';

// pipes
import { ViewNameProfessionAndSpecialtyPipe } from './pipe/view-name-profession-and-specialty.pipe';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { IsoToDatePipe } from '@presentation/pipes/iso-to-date.pipe';

@NgModule({
	declarations: [
		// routing
		EditPatientComponent,
		EditPatientComponent,
		EmergencyCareTemporaryPatientProfile,
		HomeComponent,
		NewPatientComponent,
		NewTemporaryPatientComponent,
		ProfileComponent,
		SearchComponent,
		// components
		AssignedAppointmentComponent,
		AssignedAppointmentsComponent,
		CardLicenseComponent,
		CardPatientComponent,
		CardProfessionsComponent,
		CardRolesComponent,
		EditIdentificationNumberComponent,
		EditIdentificationNumberComponent,
		FilesUploaderComponent,
		HierarchicalUnitsComponent,
		LicenseFormComponent,
		MedicalDischargeSummaryComponent,
		ProfessionSpecialtiesFormComponent,
		ResumenDeGuardiaComponent,
		SearchCreateComponent,
		SearchPatientComponent,
		ViewPatientDetailComponent,
		AppointmentHistoricDetailComponent,
		// dialogs
		AppointmentHistoricComponent,
		ArtComponent,
		EditHierarchicalUnitsComponent,
		EditLicenseComponent,
		EditPrefessionsSpecialtiesComponent,
		EditRolesComponent,
		HealthInsuranceComponent,
		MedicalCoverageComponent,
		MessageForAuditComponent,
		PrivateHealthInsuranceComponent,
		ReportsComponent,
		ScanPatientComponent,
		ScanPatientComponent,
		SearchPatientDialogComponent,
		WarningEditIdentificationNumberComponent,
		// pipes
		ViewNameProfessionAndSpecialtyPipe,
		BookingAppointmentsComponent,
	],
	exports: [
		// routing
		EditPatientComponent,
		// components
		AssignedAppointmentsComponent,
		BookingAppointmentsComponent,
		CardPatientComponent,
		ResumenDeGuardiaComponent,
		SearchPatientComponent,
	],
	imports: [
		FormsModule,
		ReactiveFormsModule,
		// routing
		PacientesRoutingModule,
		// deps
		LazyMaterialModule,
		PresentationModule,
		InternacionesModule,
		GuardiaModule,
		//Standalone Components
		EmergencyCareStatusLabelsComponent,
		IdentifierCasesComponent,
		ReferenceStateLabelComponent,
	],
	providers: [
		DateFormatPipe,
		IsoToDatePipe
	]
})
export class PacientesModule {
}
