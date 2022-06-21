import { Component } from '@angular/core';
import { Redirect } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-patient-table/internment-patient-table.component";

@Component({
	selector: 'app-internaciones-home',
	templateUrl: './internaciones-home.component.html',
	styleUrls: ['./internaciones-home.component.scss']
})
export class InternacionesHomeComponent {
	
	redirect = Redirect.HC;

	constructor() { }

}