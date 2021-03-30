import { Component, OnInit, Input } from '@angular/core';
import { VitalSign } from '@presentation/components/signo-vital-current/signo-vital.component';
import { isMoment } from '@core/utils/moment.utils';

@Component({
	selector: 'app-signo-vital-current-previous',
	templateUrl: './signo-vital-current-previous.component.html',
	styleUrls: ['./signo-vital-current-previous.component.scss']
})
export class SignoVitalCurrentPreviousComponent implements OnInit {

	@Input() vitalSign: VitalSingCurrentPrevious;

	constructor() { }

	ngOnInit(): void { }

	getDate(effectiveTime): Date {
		return (isMoment(effectiveTime)) ? effectiveTime.toDate() : effectiveTime;
	}

}

export interface VitalSingCurrentPrevious {
	description: string;
	currentValue?: VitalSign;
	previousValue?: VitalSign;
}
