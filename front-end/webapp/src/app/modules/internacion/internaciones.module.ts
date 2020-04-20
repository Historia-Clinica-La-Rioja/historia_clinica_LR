import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InternacionesRoutingModule } from './internaciones-routing.module';
import { AppMaterialModule } from 'src/app/app.material.module';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '../presentation/presentation.module';
import { AnamnesisFormComponent } from './components/routes/anamnesis-form/anamnesis-form.component';
import { InternacionesHomeComponent } from './components/routes/home/internaciones-home.component';
import { InternacionesTableComponent } from './components/internaciones-table/internaciones-table.component';
import { InternacionPacienteComponent } from './components/routes/internacion-paciente/internacion-paciente.component';
import { PatientInternmentCardComponent } from './components/patient-internment-card/patient-internment-card.component';

@NgModule({
	declarations: [
		AnamnesisFormComponent,
		InternacionesTableComponent,
		InternacionesHomeComponent,
		InternacionPacienteComponent,
		PatientInternmentCardComponent
	],
	imports: [
		CommonModule,
		AppMaterialModule,
		PresentationModule,
		CoreModule,
		InternacionesRoutingModule,
	]
})
export class InternacionesModule { }
