import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PortalPacienteComponent } from './portal-paciente.component';
import { HomeComponent } from './routes/home/home.component';


const routes: Routes = [
	{
		path: '',
		component: PortalPacienteComponent,
		children: [
			{ path: '', component: HomeComponent },
			{ path: 'home', component: HomeComponent },
		]
	},
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class PortalPacienteRoutingModule {
}
