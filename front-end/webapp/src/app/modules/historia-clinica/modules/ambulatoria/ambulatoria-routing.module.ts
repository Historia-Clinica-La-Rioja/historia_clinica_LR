import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { RoleGuard } from '@core/guards/RoleGuard';
import { PatientProfileComponent } from './routes/patient-profile/patient-profile.component';
import { AmbulatoriaPacienteComponent } from './routes/ambulatoria-paciente/ambulatoria-paciente.component';
import { NuevaConsultaComponent } from './routes/nueva-consulta/nueva-consulta.component';

const routes: Routes = [
	{
		path: '',
		children: [
			{	path: '',
				component: HomeComponent
			},
			{
				path: 'paciente/:id/profile',
				component: PatientProfileComponent
			},
			{
				path: ':idAmbulatoria/paciente/:idPaciente',
				component: AmbulatoriaPacienteComponent
			},
			{
				path: ':idAmbulatoria/paciente/:idPaciente/nueva',
				component: NuevaConsultaComponent
			},
		],
		canActivate: [RoleGuard],
		data: { allowedRoles: ['ESPECIALISTA_MEDICO', 'PROFESIONAL_DE_SALUD', 'ENFERMERO'] },
	},

];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AmbulatoriaRoutingModule { }
