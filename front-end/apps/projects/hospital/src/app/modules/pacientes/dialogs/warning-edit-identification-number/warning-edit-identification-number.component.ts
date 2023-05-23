import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-warning-edit-identification-number',
	templateUrl: './warning-edit-identification-number.component.html',
	styleUrls: ['./warning-edit-identification-number.component.scss']
})
export class WarningEditIdentificationNumberComponent implements OnInit {

	constructor(@Inject(MAT_DIALOG_DATA) public data: {
		personData: any,
		dni: string,
	}) { }

	ngOnInit(): void {
	}

}
