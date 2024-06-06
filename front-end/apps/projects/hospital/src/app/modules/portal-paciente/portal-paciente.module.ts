import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// deps
import { PresentationModule } from '@presentation/presentation.module';
import { HistoriaClinicaModule } from '../historia-clinica/historia-clinica.module';
// routing
import { PortalPacienteRoutingModule } from './portal-paciente-routing.module';
import { HomeComponent } from './routes/home/home.component';
import { MyPersonalDataComponent } from './routes/my-personal-data/my-personal-data.component';
// components
import { PortalPacienteComponent } from './portal-paciente.component';
import { ResumenHistoriaClinicaComponent } from './components/resumen-historia-clinica/resumen-historia-clinica.component';
// services
import { AmbulatoriaSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/services/ambulatoria-summary-facade.service';
import { HistoricalProblemsFacadeService } from '@historia-clinica/modules/ambulatoria/services/historical-problems-facade.service';

@NgModule({
	declarations: [
		// routing
		HomeComponent,
		MyPersonalDataComponent,
		// components
		PortalPacienteComponent,
		ResumenHistoriaClinicaComponent,
	],
	imports: [
		CommonModule,
		// routing
		PortalPacienteRoutingModule,
		// deps
		PresentationModule,
		HistoriaClinicaModule,
	],
	providers: [
		AmbulatoriaSummaryFacadeService,
		HistoricalProblemsFacadeService,
	]
})
export class PortalPacienteModule {
}
