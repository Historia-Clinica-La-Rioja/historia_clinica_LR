import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PacientesRoutingModule } from './pacientes-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { CoreModule } from '@core/core.module';
import { PacientesTableComponent } from './component/pacientes-table/pacientes-table.component';
import { SearchCreateComponent } from './component/search-create/search-create.component';
import { SearchComponent } from './routes/search/search.component';
import { NewPatientComponent } from './routes/new-patient/new-patient.component';
import { NewTemporaryPatientComponent } from './routes/new-temporary-patient/new-temporary-patient.component';
import { ProfileComponent } from './routes/profile/profile.component';
import { PresentationModule } from '../presentation/presentation.module';
import { ViewPatientDetailComponent } from './component/view-patient-detail/view-patient-detail.component';
import { EditPatientComponent } from './routes/edit-patient/edit-patient.component';
import { ScanPatientComponent } from './dialogs/scan-patient/scan-patient.component';
import { ReportsComponent } from './dialogs/reports/reports.component';

@NgModule({
	declarations: [
		HomeComponent,
		NewPatientComponent,
		NewTemporaryPatientComponent,
		PacientesTableComponent,
		ProfileComponent,
		SearchCreateComponent,
		SearchComponent,
		ViewPatientDetailComponent,
		EditPatientComponent,
  		ScanPatientComponent,
		ReportsComponent,
	],
	imports: [
		CoreModule,
		FormsModule,
		PacientesRoutingModule,
		PresentationModule,
		ReactiveFormsModule,
	]
})
export class PacientesModule {
}
