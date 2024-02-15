import { Component, Input } from '@angular/core';
import { PresentationModule } from '@presentation/presentation.module';
import { IdentifierCasesComponent, IDENTIFIER_CASES } from '../../../../hsi-components/identifier-cases/identifier-cases.component';

@Component({
	selector: 'app-available-appointment-data',
	templateUrl: './available-appointment-data.component.html',
	styleUrls: ['./available-appointment-data.component.scss'],
	standalone: true,
	imports: [IdentifierCasesComponent, PresentationModule],
})
export class AvailableAppointmentDataComponent {

	identiferCases = IDENTIFIER_CASES;

	@Input() availableAppointment: AvailableAppointmentData;

	constructor() { }

}

export interface AvailableAppointmentData {
	clinicalSpecialtyName?: string;
	practiceDescription?: string;
	date: Date;
	departmentDescription: string;
	doctorOffice: string;
	institutionName: string;
	jointDiary: boolean;
	professionalFullName: string;
}
