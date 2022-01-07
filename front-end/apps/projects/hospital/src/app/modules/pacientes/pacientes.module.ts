import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// deps
import { LazyMaterialModule } from '../lazy-material/lazy-material.module';
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { PacientesRoutingModule } from './pacientes-routing.module';
import { EditPatientComponent } from './routes/edit-patient/edit-patient.component';
import { HomeComponent } from './routes/home/home.component';
import { NewPatientComponent } from './routes/new-patient/new-patient.component';
import { NewTemporaryPatientComponent } from './routes/new-temporary-patient/new-temporary-patient.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { SearchComponent } from './routes/search/search.component';
// components
import { PacientesTableComponent } from './component/pacientes-table/pacientes-table.component';
import { SearchCreateComponent } from './component/search-create/search-create.component';
import { ViewPatientDetailComponent } from './component/view-patient-detail/view-patient-detail.component';
// dialogs
import { ReportsComponent } from './dialogs/reports/reports.component';
import { ScanPatientComponent } from './dialogs/scan-patient/scan-patient.component';
import { EditProfessionsComponent } from './dialogs/edit-professions/edit-professions.component';

@NgModule({
	declarations: [
		// routing
		EditPatientComponent,
		HomeComponent,
		NewPatientComponent,
		NewTemporaryPatientComponent,
		ProfileComponent,
		SearchComponent,
		// components
		PacientesTableComponent,
		SearchCreateComponent,
		ViewPatientDetailComponent,
		// dialogs
		EditPatientComponent,
		ScanPatientComponent,
		EditProfessionsComponent,
		ReportsComponent,
		ScanPatientComponent,
	],
	imports: [
		FormsModule,
		ReactiveFormsModule,
		// routing
		PacientesRoutingModule,
		// deps
		LazyMaterialModule,
		PresentationModule,
	]
})
export class PacientesModule {
}
