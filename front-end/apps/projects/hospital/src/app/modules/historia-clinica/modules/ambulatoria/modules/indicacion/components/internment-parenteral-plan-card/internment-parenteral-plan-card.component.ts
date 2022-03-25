import { Component, OnInit } from '@angular/core';
import { PARENTERAL_PLAN } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";

@Component({
	selector: 'app-internment-parenteral-plan-card',
	templateUrl: './internment-parenteral-plan-card.component.html',
	styleUrls: ['./internment-parenteral-plan-card.component.scss']
})
export class InternmentParenteralPlanCardComponent implements OnInit {

	PARENTERAL_PLAN = PARENTERAL_PLAN;

	constructor() { }

	ngOnInit(): void { }

}
