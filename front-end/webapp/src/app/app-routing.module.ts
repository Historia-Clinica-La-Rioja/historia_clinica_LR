import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InicioComponent } from './modules/core/route/inicio/inicio.component';

const routes: Routes = [
	{
		path: '',
		component: InicioComponent,
		pathMatch: 'full',
	},
	{
		path: 'pacientes',
		loadChildren: () => import('./modules/pacientes/pacientes.module').then(m => m.PacientesModule)
	},
];

@NgModule({
	imports: [RouterModule.forRoot(routes, {enableTracing: true, scrollPositionRestoration: 'enabled'})],
	exports: [RouterModule]
})
export class AppRoutingModule {
}
