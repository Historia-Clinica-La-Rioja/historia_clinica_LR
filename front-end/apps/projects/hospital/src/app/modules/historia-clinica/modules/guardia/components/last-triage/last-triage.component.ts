import { Component, Input } from '@angular/core';
import { EmergencyCareTypes } from '../../constants/masterdata';
import { TriageDetails } from '../triage-details/triage-details.component';

@Component({
	selector: 'app-last-triage',
	templateUrl: './last-triage.component.html',
	styleUrls: ['./last-triage.component.scss']
})
export class LastTriageComponent {

	@Input() triage: TriageDetails;
	@Input() emergencyCareType: EmergencyCareTypes;

	constructor() { }
}
