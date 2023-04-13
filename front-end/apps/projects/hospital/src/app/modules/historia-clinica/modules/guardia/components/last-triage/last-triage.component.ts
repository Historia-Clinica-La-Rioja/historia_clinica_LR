import { Component, EventEmitter, Input, Output, } from '@angular/core';
import { EmergencyCareTypes } from '../../constants/masterdata';
import { RiskFactorFull, Triage } from '../triage-details/triage-details.component';

@Component({
	selector: 'app-last-triage',
	templateUrl: './last-triage.component.html',
	styleUrls: ['./last-triage.component.scss']
})
export class LastTriageComponent {

	@Input() triage: Triage;
	@Input() emergencyCareType: EmergencyCareTypes;
	@Output() triageRiskFactors = new EventEmitter<RiskFactorFull[]>();

	constructor() { }

	emit(event) {
		this.triageRiskFactors.emit(event);
	}
}
