import { Component, OnInit, Input } from '@angular/core';
import { Moment } from 'moment';

@Component({
	selector: 'app-signo-vital-current-previous',
	templateUrl: './signo-vital-current-previous.component.html',
	styleUrls: ['./signo-vital-current-previous.component.scss']
})
export class SignoVitalCurrentPreviousComponent implements OnInit {

	@Input('data')
	vitalSign: VitalSingCurrentPrevious;

	constructor() { }

	ngOnInit(): void { }

}

export interface VitalSingCurrentPrevious {
	description: string;
	currentValue?: {
		value: number,
		effectiveTime: Moment,
	};
	previousValue?: {
		value: number,
		effectiveTime: Moment,
	};
}
