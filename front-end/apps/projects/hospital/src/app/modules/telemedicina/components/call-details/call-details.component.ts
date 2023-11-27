import { Component, Input } from '@angular/core';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { timeDifference } from '@core/utils/date.utils';
import { Priority } from '@presentation/components/priority/priority.component';
import { PresentationModule } from '@presentation/presentation.module';
import { PatientSummary, PatientSummaryComponent } from '../../../hsi-components/patient-summary/patient-summary.component';


@Component({
	selector: 'app-call-details',
	templateUrl: './call-details.component.html',
	styleUrls: ['./call-details.component.scss'],
	standalone: true,
	imports: [PresentationModule, PatientSummaryComponent]
})
export class CallDetailsComponent {

	timeDifference = timeDifference;
	dateTimeDtotoLocalDate = dateTimeDtotoLocalDate;
	@Input() callDetails: CallDetails
	constructor() { }
}

export interface CallDetails {
	link: string;
	patient: PatientSummary;
	professionalFullName: string
	priority: Priority;
	createdOn: Date;
	institutionName: string;
	clinicalSpecialty: string;
}
