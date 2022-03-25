import { Component, OnInit } from '@angular/core';
import { OTHER_INDICATION } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";

@Component({
	selector: 'app-internment-other-indication-card',
	templateUrl: './internment-other-indication-card.component.html',
	styleUrls: ['./internment-other-indication-card.component.scss']
})
export class InternmentOtherIndicationCardComponent implements OnInit {

	OTHER_INDICATION = OTHER_INDICATION;

	constructor() { }

	ngOnInit(): void { }

}
