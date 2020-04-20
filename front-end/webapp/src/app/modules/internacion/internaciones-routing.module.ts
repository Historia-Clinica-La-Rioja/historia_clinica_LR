import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InternacionesHomeComponent } from './components/routes/home/internaciones-home.component';
import { InternacionPacienteComponent } from './components/routes/internacion-paciente/internacion-paciente.component';
import { AnamnesisComponent } from './components/routes/anamnesis/anamnesis.component';


const routes: Routes = [
	{
		path: '',
		component: InternacionesHomeComponent,
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente',
		component: InternacionPacienteComponent,
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/anamnesis',
		component: AnamnesisComponent
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class InternacionesRoutingModule { }
