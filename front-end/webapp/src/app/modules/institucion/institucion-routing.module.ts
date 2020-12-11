import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ERole } from '@api-rest/api-model';
import { RoleGuard } from '@core/guards/RoleGuard';

import { InstitucionComponent } from './institucion.component';
import { HomeComponent } from './routes/home/home.component';

const routes: Routes = [
	{
		path: ':id',
		component: InstitucionComponent,
		children: [
			{ path: '', component: HomeComponent },
			{
				path: 'pacientes',
				loadChildren: () => import('../pacientes/pacientes.module').then(m => m.PacientesModule),
			},
			{
				path: 'internaciones',
				loadChildren: () => import('../historia-clinica/modules/internacion/internaciones.module').then(m => m.InternacionesModule),
			},
			{
				path: 'ambulatoria',
				loadChildren: () => import('../historia-clinica/modules/ambulatoria/ambulatoria.module').then(m => m.AmbulatoriaModule),
			},
			{
				path: 'turnos',
				loadChildren: () => import('../turnos/turnos.module').then(m => m.TurnosModule),
			},
			{
				path: 'camas',
				loadChildren: () => import('../camas/camas.module').then(m => m.CamasModule),
			},
			{
				path: 'guardia',
				loadChildren: () => import('../guardia/guardia.module').then(m => m.GuardiaModule),
			},
		],
		canActivate: [RoleGuard],
		data: { allowedRoles: [ERole.ADMINISTRADOR, ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.ADMINISTRATIVO,
								ERole.ENFERMERO, ERole.ENFERMERO_ADULTO_MAYOR, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ROOT] },

	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class InstitucionRoutingModule { }
