import { Component, Input } from '@angular/core';
import { Episode } from '../emergency-care-episodes-summary/emergency-care-episodes-summary.component';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { EmergencyCareStatusLabels } from '@hsi-components/emergency-care-status-labels/emergency-care-status-labels.component';

@Component({
	selector: 'app-emergency-care-patient-discharge-state',
	templateUrl: './emergency-care-patient-discharge-state.component.html',
	styleUrls: ['./emergency-care-patient-discharge-state.component.scss']
})
export class EmergencyCarePatientDischargeStateComponent {

	readonly REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
	medicalDischargeCreator: RegisterEditor;
	statusLabel: EmergencyCareStatusLabels;

	@Input() set episode(episode: Episode) {
		this.buildPatientDischargeInformation(episode);
	}

	constructor() { }

	buildPatientDischargeInformation(episode: Episode) {
		const dischargeSummary = episode.dischargeSummary;
		this.medicalDischargeCreator = {
			createdBy: `${dischargeSummary.medicalDischargeProfessionalName} ${dischargeSummary.medicalDischargeProfessionalLastName}`,
			date: dateTimeDtotoLocalDate(dischargeSummary.medicalDischargeOn)
		}

		this.statusLabel = { stateId: episode.state.id, description: "guardia.home.episodes.episode.status.PATIENT_DISCHARGE" };
	}

}
