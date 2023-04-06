import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-confirmed-fusion',
	templateUrl: './confirmed-fusion.component.html',
	styleUrls: ['./confirmed-fusion.component.scss']
})
export class ConfirmedFusionComponent implements OnInit {

	constructor(@Inject(MAT_DIALOG_DATA) public data: {
		idPatient: string,
	}) { }

	ngOnInit(): void {
	}

}
