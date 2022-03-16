import { Component, OnInit } from '@angular/core';
import { INTERNMENT_INDICATIONS } from "@historia-clinica/constants/summaries";

@Component({
	selector: 'app-internment-indications-card',
	templateUrl: './internment-indications-card.component.html',
	styleUrls: ['./internment-indications-card.component.scss']
})
export class InternmentIndicationsCardComponent implements OnInit {

	internmentIndication = INTERNMENT_INDICATIONS;

	constructor() { }

	ngOnInit(): void { }

}