import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";
import { PresentationModule } from "@presentation/presentation.module";

import { CardMedicacionesComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/components/card-medicaciones/card-medicaciones.component";
import { InternmentIndicationsCardComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/components/internment-indications-card/internment-indications-card.component";
import { ItemPrescripcionesComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/components/item-prescripciones/item-prescripciones.component";
import { NuevaPrescripcionComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/dialogs/nueva-prescripcion/nueva-prescripcion.component";
import { SuspenderMedicacionComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/dialogs/suspender-medicacion/suspender-medicacion.component";
import { DietComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/dialogs/diet/diet.component";

@NgModule({
	declarations: [
		CardMedicacionesComponent,
		ItemPrescripcionesComponent,
		InternmentIndicationsCardComponent,
		NuevaPrescripcionComponent,
		SuspenderMedicacionComponent,
		DietComponent,
	],
	exports: [
		CardMedicacionesComponent,
		ItemPrescripcionesComponent,
		InternmentIndicationsCardComponent,
	],
	imports: [
		CommonModule,
		FormsModule,
		PresentationModule
	]
})
export class IndicacionModule { }
