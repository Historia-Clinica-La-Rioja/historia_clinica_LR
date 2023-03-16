import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './routes/home/home.component';
import { PatientsFusionComponent } from './routes/patients-fusion/patients-fusion.component';

const routes: Routes = [{
	path: '',
	children: [
		{
			path: '',
			component: HomeComponent
		},
		{
			path:"fusion-pacientes",
			component: PatientsFusionComponent
		}
	]
}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class AuditoriaRoutingModule { }
