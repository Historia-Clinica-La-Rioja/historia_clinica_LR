import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PortalPacienteRoutingModule } from './portal-paciente-routing.module';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';
import { PortalPacienteComponent } from './portal-paciente.component';
import { HomeComponent } from './routes/home/home.component';


@NgModule({
	declarations: [PortalPacienteComponent, HomeComponent],
	imports: [
		CommonModule,
		CoreModule,
		PresentationModule,
		PortalPacienteRoutingModule
	]
})
export class PortalPacienteModule {
}
