import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
	{
		path: '',
		redirectTo: 'pacientes',
		pathMatch: 'full',
	},
	{
		path: 'pacientes',
		loadChildren: () => import('./modules/pacientes/pacientes.module').then(m => m.PacientesModule)
	},
	{
		path: 'internaciones',
		loadChildren: () => import('./modules/internacion/internaciones.module').then(m => m.InternacionesModule)
	},
];

@NgModule({
	imports: [RouterModule.forRoot(routes, {enableTracing: true, scrollPositionRestoration: 'enabled'})],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
