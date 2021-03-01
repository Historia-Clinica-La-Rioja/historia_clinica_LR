import { Component, Input, OnInit } from '@angular/core';
import { Moment } from 'moment';

@Component({
	selector: 'app-signo-vital',
	templateUrl: './signo-vital.component.html',
	styleUrls: ['./signo-vital.component.scss']
})
export class SignoVitalComponent implements OnInit {

	@Input() description: string;
	@Input() vitalSign: VitalSign;

	constructor() {
	}

	ngOnInit(): void {
	}

}

export interface VitalSign {
	value?: number;
	effectiveTime?: Moment | Date;
}
