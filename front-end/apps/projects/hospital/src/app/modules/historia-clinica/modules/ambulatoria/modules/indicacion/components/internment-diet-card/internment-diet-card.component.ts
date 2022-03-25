import { Component, OnInit } from '@angular/core';
import { DIET } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";

@Component({
	selector: 'app-internment-diet-card',
	templateUrl: './internment-diet-card.component.html',
	styleUrls: ['./internment-diet-card.component.scss']
})
export class InternmentDietCardComponent implements OnInit {

	DIET = DIET;

	constructor() { }

	ngOnInit() { }

}
