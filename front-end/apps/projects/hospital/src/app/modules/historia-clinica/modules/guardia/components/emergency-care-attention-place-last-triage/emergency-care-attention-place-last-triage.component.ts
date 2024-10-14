import { Component, Input } from '@angular/core';
import { EmergencyCareEpisodeListTriageDto } from '@api-rest/api-model';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { PatientNameService } from '@core/services/patient-name.service';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';

@Component({
	selector: 'app-emergency-care-attention-place-last-triage',
	templateUrl: './emergency-care-attention-place-last-triage.component.html',
	styleUrls: ['./emergency-care-attention-place-last-triage.component.scss']
})
export class EmergencyCareAttentionPlaceLastTriageComponent {

	readonly IDENTIFIER_CASES = IDENTIFIER_CASES;
	readonly REGISTER_EDITOR_CASES = REGISTER_EDITOR_CASES;

	_lastTriage: EmergencyCareEpisodeListTriageDto;
	reasons: string[] = [];
	registerEditorInfo: RegisterEditor | null = null;

	@Input() set lastTriage(lastTriage: EmergencyCareEpisodeListTriageDto) {
		if (lastTriage) {
			this._lastTriage = lastTriage;
			this.reasons = lastTriage.reasons.map(reason => reason.snomed.pt);
			this.registerEditorInfo = this.buildRegisterEditorInfo(lastTriage);
		}
	}

	constructor(private readonly patientNameService: PatientNameService) { }

	private buildRegisterEditorInfo(lastTriage: EmergencyCareEpisodeListTriageDto): RegisterEditor {
		const createdBy = this.patientNameService.completeName(
			lastTriage.creator.firstName,
			lastTriage.creator.nameSelfDetermination,
			lastTriage.creator.lastName,
			lastTriage.creator.middleNames,
			lastTriage.creator.otherLastNames
		);
		const date = dateTimeDtotoLocalDate(lastTriage.createdOn);

		return { createdBy, date };
	}
}
