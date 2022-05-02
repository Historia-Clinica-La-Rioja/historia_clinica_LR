import { Component, OnInit } from '@angular/core';
import { PHARMACO } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";

@Component({
	selector: 'app-internment-pharmaco-card',
	templateUrl: './internment-pharmaco-card.component.html',
	styleUrls: ['./internment-pharmaco-card.component.scss']
})
export class InternmentPharmacoCardComponent implements OnInit {

	PHARMACO = PHARMACO;

	constructor() { }

	ngOnInit(): void { }

}
