import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { HCEImmunizationDto } from '@api-rest/api-model';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';

@Component({
	selector: 'app-detalle-vacuna',
	templateUrl: './detalle-vacuna.component.html',
	styleUrls: ['./detalle-vacuna.component.scss']
})
export class DetalleVacunaComponent implements OnInit {

	private readonly name: string;

	constructor(
		public dialogRef: MatDialogRef<HCEImmunizationDto>,
		@Inject(MAT_DIALOG_DATA) public data: {
			title: string,
			tradename: string,
			dose: string,
			date: string,
			lot: number,
			institution: string,
			professional: string,
			terms: string,
			scheme: string,
			observations: string,
		}) { }

	ngOnInit(): void {
	}

	toFormatDate = (date: string) => {
		return momentFormat(momentParseDate(date), DateFormat.VIEW_DATE);
	}
}
