import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InternacionesRoutingModule } from './internaciones-routing.module';
import { AppMaterialModule } from 'src/app/app.material.module';
import { CoreModule } from '@core/core.module';
import { InternacionesHomeComponent } from './components/routes/home/internaciones-home.component';
import { InternacionesTableComponent } from './components/internaciones-table/internaciones-table.component';
import { InternacionPacienteComponent } from './components/routes/internacion-paciente/internacion-paciente.component';
import { PresentationModule } from '../presentation/presentation.module';


@NgModule({
	declarations: [
		InternacionesTableComponent,
		InternacionesHomeComponent,
		InternacionPacienteComponent
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
