import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { RoleGuard } from '@core/guards/RoleGuard';
import { PatientProfileComponent } from './routes/patient-profile/patient-profile.component';

const routes: Routes = [
	{
		path: '',
		children: [
			{	path: '',
				component: HomeComponent}
			,
			{
				path: 'paciente/:id',
				component: PatientProfileComponent
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
