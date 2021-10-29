import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';
import { HistoriaClinicaModule } from '../historia-clinica/historia-clinica.module';

import { PortalPacienteRoutingModule } from './portal-paciente-routing.module';

import { PortalPacienteComponent } from './portal-paciente.component';
import { HomeComponent } from './routes/home/home.component';
import { ResumenHistoriaClinicaComponent } from './components/resumen-historia-clinica/resumen-historia-clinica.component';
import { MyPersonalDataComponent } from './routes/my-personal-data/my-personal-data.component';

@NgModule({
	declarations: [
		PortalPacienteComponent,
		HomeComponent,
		ResumenHistoriaClinicaComponent,
		MyPersonalDataComponent,
	],
	imports: [
		CommonModule,
		CoreModule,
		PresentationModule,
		HistoriaClinicaModule,
		PortalPacienteRoutingModule,
	]
})
export class PortalPacienteModule {
}
