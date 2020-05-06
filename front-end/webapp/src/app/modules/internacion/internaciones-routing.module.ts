import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InternacionesHomeComponent } from './routes/home/internaciones-home.component';
import { InternacionPacienteComponent } from './routes/internacion-paciente/internacion-paciente.component';
import { AnamnesisComponent } from './routes/anamnesis/anamnesis.component';
import { NewInternmentComponent } from "./routes/new-internment/new-internment.component";
import { EpicrisisComponent } from './routes/epicrisis/epicrisis.component';
import { NotaEvolucionComponent } from './routes/nota-evolucion/nota-evolucion.component';


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
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/anamnesis/:anamnesisId',
		component: AnamnesisComponent
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/nota-evolucion',
		component: NotaEvolucionComponent
	},
	{
		path: 'internacion/:idInternacion/paciente/:idPaciente/epicrisis',
		component: EpicrisisComponent
	},
	{
		path: 'internacion/new',
		component: NewInternmentComponent
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class InternacionesRoutingModule { }
