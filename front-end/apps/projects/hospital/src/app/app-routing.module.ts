import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { BackofficeComponent } from '@core/components/backoffice/backoffice.component';

/**
 * Routes principales
 */
export enum AppRoutes {
	Auth = 'auth',					// pantallas de ingreso
	Backoffice = 'backoffice',		// pantalla de redirección a Backoffice
	Home = 'home',					// pantallas del sistema general (perfil de usuario, configuración del sistema, etc)
	Institucion = 'institucion', 	// funcionalidad en la institución (historia clínica, pacientes, turnos, etc)
	PortalPaciente = 'paciente',	// portal del paciente
}

const routes: Routes = [
	{
		path: '',
		redirectTo: AppRoutes.Home,
		pathMatch: 'full',
	},
	{
		path: AppRoutes.Auth,
		loadChildren: () => import('./modules/auth/auth.module').then(m => m.AuthModule),
	},
	{
		path: AppRoutes.Home,
		loadChildren: () => import('./modules/home/home.module').then(m => m.HomeModule),
	},
	{
		path: AppRoutes.Institucion,
		loadChildren: () => import('./modules/institucion/institucion.module').then(m => m.InstitucionModule),
	},
	{
		path: AppRoutes.PortalPaciente,
		loadChildren: () => import('./modules/portal-paciente/portal-paciente.module').then(m => m.PortalPacienteModule),
	},
	{
		path: AppRoutes.Backoffice,
		component: BackofficeComponent,
	},
	{
		path: '**',
		redirectTo: AppRoutes.Home,
	},
];

@NgModule({
	imports: [RouterModule.forRoot(routes, {
	// habilitar log de routeos en consola
	// enableTracing: true,
	scrollPositionRestoration: 'enabled',
	relativeLinkResolution: 'legacy'
})],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
