import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HCEImmunizationDto } from '@api-rest/api-model';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-detalle-vacuna',
	templateUrl: './detalle-vacuna.component.html',
	styleUrls: ['./detalle-vacuna.component.scss']
})
export class DetalleVacunaComponent {

	Color = Color;

	constructor(
		public dialogRef: MatDialogRef<HCEImmunizationDto>,
		@Inject(MAT_DIALOG_DATA) public data: {
			vaccineTitleName: string,
			appliedDoses: string,
			applicationDate: string,
			lotNumber: string,
			institutionName: string,
			ProfessionalCompleteName: string,
			vaccineConditinDescription: string,
			vaccinationSchemeDescription: string,
			vaccineObservations: string,
		}) { }
}
