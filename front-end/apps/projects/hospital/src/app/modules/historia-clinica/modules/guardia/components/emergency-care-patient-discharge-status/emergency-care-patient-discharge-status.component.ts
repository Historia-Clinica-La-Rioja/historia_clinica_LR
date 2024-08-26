import { Component, Input, OnInit } from '@angular/core';
import { Episode } from '../emergency-care-patients-summary/emergency-care-patients-summary.component';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-emergency-care-patient-discharge-status',
	templateUrl: './emergency-care-patient-discharge-status.component.html',
	styleUrls: ['./emergency-care-patient-discharge-status.component.scss']
})
export class EmergencyCarePatientDischargeStatusComponent implements OnInit {

	readonly REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
	readonly BLUE = Color.BLUE;
	medicalDischargeCreator: RegisterEditor;

	@Input() set episode(episode: Episode) {
		const dischargeSummary = episode.dischargeSummary;
		this.medicalDischargeCreator = {
			createdBy: `${dischargeSummary.medicalDischargeProfessionalName} ${dischargeSummary.medicalDischargeProfessionalLastName}`,
			date: dateTimeDtoToDate(dischargeSummary.medicalDischargeOn)
		}
	}

	constructor() { }

	ngOnInit(): void {
	}

}
