import { Component, Input } from '@angular/core';
import { DateTimeDto } from '@api-rest/api-model';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';

@Component({
	selector: 'app-appointment-summary',
	templateUrl: './appointment-summary.component.html',
	styleUrls: ['./appointment-summary.component.scss']
})
export class AppointmentSummaryComponent {

	identiferCases = IDENTIFIER_CASES;
	@Input() appointmentSummary: AppointmentSummary;

	constructor() { }

}

export interface AppointmentSummary {
	state: ColoredLabel;
	date?: DateTimeDto;
	professionalFullName?: string;
	institution?: string;
}