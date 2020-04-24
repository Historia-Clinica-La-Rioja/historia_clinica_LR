import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
	// ver AuthRoutingModule para /auth/login o /auth/reset-password
	{
		path: '',
		redirectTo: 'pacientes',
		pathMatch: 'full',
	},
	// ver PacientesRoutingModule para /pacientes, /pacientes/search, etc
	{
		// Si el usuario ingresa a internaciones/ se carga el InternacionesModule
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
