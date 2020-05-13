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
		]
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class InstitucionRoutingModule { }
