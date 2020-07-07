import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './routes/home/home.component';
import { AmbulatoriaRoutingModule } from './ambulatoria-routing.module';
import { FormsModule } from '@angular/forms';
import { CoreModule } from '@core/core.module';
import { PacientesModule } from '../../../pacientes/pacientes.module';
import { PresentationModule } from '@presentation/presentation.module';
import { PatientProfileComponent } from './routes/patient-profile/patient-profile.component';
import { AmbulatoriaPacienteComponent } from './routes/ambulatoria-paciente/ambulatoria-paciente.component';
import { ResumenComponent } from './components/resumen/resumen.component';
import { ProblemasComponent } from './components/problemas/problemas.component';
import { HistoriaClinicaModule } from "../../historia-clinica.module";


@NgModule({
	declarations: [
		HomeComponent,
		PatientProfileComponent,
		AmbulatoriaPacienteComponent,
		ResumenComponent,
		ProblemasComponent
	],
	imports: [
		AmbulatoriaRoutingModule,
		FormsModule,
		CommonModule,
		CoreModule,
		HistoriaClinicaModule,
		PacientesModule,
		PresentationModule
	]
})
export class AmbulatoriaModule {
}
