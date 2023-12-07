import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-confirmed-fusion',
	templateUrl: './confirmed-fusion.component.html',
	styleUrls: ['./confirmed-fusion.component.scss']
})
export class ConfirmedFusionComponent implements OnInit {
	isFusion:boolean=true;
	constructor(@Inject(MAT_DIALOG_DATA) public data: {
		idPatients: string[],
	}) { }

	ngOnInit(): void {
		if(!this.data.idPatients.length){
			this.isFusion=true;
		}else{
			this.isFusion=false;
		}
	}

}
