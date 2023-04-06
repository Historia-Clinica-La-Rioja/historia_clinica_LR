import { Component, Inject, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-warning-fusion',
	templateUrl: './warning-fusion.component.html',
	styleUrls: ['./warning-fusion.component.scss']
})
export class WarningFusionComponent implements OnInit {

	constructor(@Inject(MAT_DIALOG_DATA) public data: {
		cant: number,
		fullName:string,
		identification:string,
		birthDate:string,
		idPatient:string,
	}) {
	 }

	ngOnInit(): void {

	}

}
