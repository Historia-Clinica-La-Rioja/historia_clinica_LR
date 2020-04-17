import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppMaterialModule } from 'src/app/app.material.module';
import { PatientCardComponent } from './patient-card/patient-card.component';
import { CoreModule } from '@core/core.module';


@NgModule({
	declarations: [
	PatientCardComponent],
	imports: [
		CommonModule,
		AppMaterialModule,
		CoreModule,
	],
	exports: [
		PatientCardComponent
	]
})
export class PresentationModule { }
