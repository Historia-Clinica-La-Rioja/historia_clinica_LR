import { Component, Input } from '@angular/core';
import { EpisodeListTriage } from '../emergency-care-patients-summary/emergency-care-patients-summary.component';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';

@Component({
	selector: 'app-emergency-care-triage-summary-item',
	templateUrl: './emergency-care-triage-summary-item.component.html',
	styleUrls: ['./emergency-care-triage-summary-item.component.scss']
})
export class EmergencyCareTriageSummaryItemComponent {

	readonly REASON = IDENTIFIER_CASES.REASON;
	triageCreator: RegisterEditor;
	triage: EpisodeListTriage;

	@Input() set episodeListTriage(episodeListTriage: EpisodeListTriage) {
		this.triage = episodeListTriage;
		const hasTriage = !!episodeListTriage.emergencyCareEpisodeListTriageDto.creator;

		if (hasTriage) {
			const { firstName, lastName } = this.triage.emergencyCareEpisodeListTriageDto.creator;
			this.setTriageCreatorData(firstName, lastName);
		}
	};

	constructor() { }

	private setTriageCreatorData(firstName: string, lastName: string) {
		this.triageCreator = {
			createdBy: `${firstName} ${lastName}`
		}
	}

}
