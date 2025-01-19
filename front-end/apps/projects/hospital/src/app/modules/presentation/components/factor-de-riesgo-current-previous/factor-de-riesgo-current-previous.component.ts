import { Component, Input } from '@angular/core';
import { RiskFactor } from '@presentation/components/factor-de-riesgo-current/factor-de-riesgo.component';
import { fixDate } from '@core/utils/date/format';

@Component({
	selector: 'app-factor-de-riesgo-current-previous',
	templateUrl: './factor-de-riesgo-current-previous.component.html',
	styleUrls: ['./factor-de-riesgo-current-previous.component.scss']
})
export class FactorDeRiesgoCurrentPreviousComponent {

	@Input() riskFactor: RiskFactorCurrentPrevious;

	constructor() { }

	getDate(effectiveTime): Date {
		return fixDate(effectiveTime)
	}

}

export interface RiskFactorCurrentPrevious {
	description: string;
	currentValue?: RiskFactor;
	previousValue?: RiskFactor;
}