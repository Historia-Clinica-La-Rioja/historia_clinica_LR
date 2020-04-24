import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InternacionesRoutingModule } from './internaciones-routing.module';
import { AppMaterialModule } from 'src/app/app.material.module';
import { CoreModule } from '@core/core.module';
import { PresentationModule } from '../presentation/presentation.module';
import { AnamnesisFormComponent } from './components/anamnesis-form/anamnesis-form.component';
import { InternacionesHomeComponent } from './components/routes/home/internaciones-home.component';
import { InternacionesTableComponent } from './components/internaciones-table/internaciones-table.component';
import { InternacionPacienteComponent } from './components/routes/internacion-paciente/internacion-paciente.component';
import { AnamnesisComponent } from './components/routes/anamnesis/anamnesis.component';
import { AntecentesPersonalesComponent } from './components/antecentes-personales/antecentes-personales.component';
import { ConceptsSearchDialogComponent } from './dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { ConceptsSearchComponent } from './components/concepts-search/concepts-search.component';
import { FormsModule } from '@angular/forms';
import { DiagnosticosComponent } from './components/diagnosticos/diagnosticos.component';

@NgModule({
	declarations: [
		AnamnesisFormComponent,
		InternacionesTableComponent,
		InternacionesHomeComponent,
		InternacionPacienteComponent,
		AnamnesisComponent,
		AntecentesPersonalesComponent,
		ConceptsSearchDialogComponent,
		ConceptsSearchComponent,
		DiagnosticosComponent,
	],
	imports: [
		AppMaterialModule,
		CoreModule,
		CommonModule,
		FormsModule,
		InternacionesRoutingModule,
		PresentationModule,
	]
})
export class InternacionesModule { }
