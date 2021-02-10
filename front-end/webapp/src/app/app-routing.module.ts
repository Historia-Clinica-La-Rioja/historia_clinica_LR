import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
	{
		path: '',
		redirectTo: 'home',
		pathMatch: 'full',
	},
	{
		path: 'home',
		loadChildren: () => import('./modules/home/home.module').then(m => m.HomeModule),
	},
	{
		path: 'institucion',
		loadChildren: () => import('./modules/institucion/institucion.module').then(m => m.InstitucionModule),
	},
];

@NgModule({
	imports: [RouterModule.forRoot(routes, {
		// habilitar log de routeos en consola
		// enableTracing: true,
		scrollPositionRestoration: 'enabled'
	})],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
