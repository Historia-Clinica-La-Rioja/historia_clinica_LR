import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

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
				loadChildren: () => import('../internacion/internaciones.module').then(m => m.InternacionesModule),
			},
		]
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class InstitucionRoutingModule { }
