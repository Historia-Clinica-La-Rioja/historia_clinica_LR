import { Component } from '@angular/core';
import { Redirect } from '../../components/internment-patient-card/internment-patient-card.component';
@Component({
	selector: 'app-internaciones-home',
	templateUrl: './internaciones-home.component.html',
	styleUrls: ['./internaciones-home.component.scss']
})
export class InternacionesHomeComponent {
	
	redirect = Redirect.HC;

	constructor() { }

}