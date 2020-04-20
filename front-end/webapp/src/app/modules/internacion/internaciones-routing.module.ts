import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InternacionesHomeComponent } from './components/routes/home/internaciones-home.component';
import { InternacionPacienteComponent } from './components/routes/internacion-paciente/internacion-paciente.component';
import { AnamnesisFormComponent } from './components/routes/anamnesis-form/anamnesis-form.component';


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
		path: 'anamnesis',
		component: AnamnesisFormComponent
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class InternacionesRoutingModule { }
