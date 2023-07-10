import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-warning-fusion',
	templateUrl: './warning-fusion.component.html',
	styleUrls: ['./warning-fusion.component.scss']
})
export class WarningFusionComponent implements OnInit {

	constructor(@Inject(MAT_DIALOG_DATA) public data: {
		title?:string;
		cant: number,
		fullName:string,
		identification:string,
		birthDate:string,
		idPatient:string,
		nameSelfDetermination:string,
		labelButtonConfirm:string,
	}) {
	 }

	ngOnInit(): void {

	}

}
