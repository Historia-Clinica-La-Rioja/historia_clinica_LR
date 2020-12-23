import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GuardiaRoutingModule } from './guardia-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { AdmisionAdministrativaComponent } from './routes/admision-administrativa/admision-administrativa.component';
import { CoreModule } from '@core/core.module';
import { InstitucionModule } from '../institucion/institucion.module';
import { PresentationModule } from '@presentation/presentation.module';
import { TriageComponent } from './components/triage/triage.component';
import { AdministrativeTriageComponent } from './components/administrative-triage/administrative-triage.component';
import { NewEpisodeAdminTriageComponent } from './routes/new-episode-admin-triage/new-episode-admin-triage.component';
import { AdministrativeTriageDialogComponent } from './dialogs/administrative-triage-dialog/administrative-triage-dialog.component';

import { HistoriaClinicaModule } from '../historia-clinica/historia-clinica.module';

@NgModule({
	declarations: [
		HomeComponent,
		TriageComponent,
		AdministrativeTriageComponent,
		NewEpisodeAdminTriageComponent,
		AdmisionAdministrativaComponent,
		AdministrativeTriageDialogComponent
	],
	imports: [
		CommonModule,
		GuardiaRoutingModule,
		CoreModule,
		PresentationModule,
		GuardiaRoutingModule,
		InstitucionModule,
		PresentationModule,
		HistoriaClinicaModule, // TODO Quitar este modulo
	]
})
export class GuardiaModule {
}
