import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { RoleGuard } from '@core/guards/RoleGuard';
import { PatientProfileComponent } from './routes/patient-profile/patient-profile.component';
import { AmbulatoriaPacienteComponent } from './routes/ambulatoria-paciente/ambulatoria-paciente.component';
import { ERole } from '@api-rest/api-model';

const routes: Routes = [
	{
		path: '',
		children: [
			{	path: '',
				component: HomeComponent
			},
			{
				path: 'paciente/:idPaciente/profile',
				component: PatientProfileComponent
			},
			{
				path: 'paciente/:idPaciente',
				component: AmbulatoriaPacienteComponent
			}
		],
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA] },
	},

];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AmbulatoriaRoutingModule { }
