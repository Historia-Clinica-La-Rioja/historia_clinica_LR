import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PortalPacienteRoutingModule } from './portal-paciente-routing.module';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';
import { PortalPacienteComponent } from './portal-paciente.component';
import { HomeComponent } from './routes/home/home.component';
import { ResumenHistoriaClinicaComponent } from './components/resumen-historia-clinica/resumen-historia-clinica.component';
import {HistoriaClinicaModule} from '../historia-clinica/historia-clinica.module';


@NgModule({
	declarations: [PortalPacienteComponent, HomeComponent, ResumenHistoriaClinicaComponent],
	imports: [
		CommonModule,
		CoreModule,
		PresentationModule,
		PortalPacienteRoutingModule,
		HistoriaClinicaModule
	]
})
export class PortalPacienteModule {
}
