import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
	{
		path: '',
		redirectTo: 'pacientes',
		pathMatch: 'full',
	},
	// Si el usuario ingresa a pacientes/ se carga el PacientesModule
	{
		path: 'pacientes',
		loadChildren: () => import('./modules/pacientes/pacientes.module').then(m => m.PacientesModule)
	},
	// Si el usuario ingresa a internaciones/ se carga el InternacionesModule
	{
		path: 'internaciones',
		loadChildren: () => import('./modules/internacion/internaciones.module').then(m => m.InternacionesModule)
	},
];

@NgModule({
	imports: [RouterModule.forRoot(routes, {
		// habilitar log de routeos en consola
		//enableTracing: true,
		scrollPositionRestoration: 'enabled'
	})],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
