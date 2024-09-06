import { Component, Input } from '@angular/core';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';
import { Episode } from '../emergency-care-patients-summary/emergency-care-patients-summary.component';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { EmergencyCareStatusLabels } from '@hsi-components/emergency-care-status-labels/emergency-care-status-labels.component'

@Component({
	selector: 'app-emergency-care-in-attention-state',
	templateUrl: './emergency-care-in-attention-state.component.html',
	styleUrls: ['./emergency-care-in-attention-state.component.scss']
})
export class EmergencyCareInAttentionStateComponent {

	readonly IDENTIFIER_CASES = IDENTIFIER_CASES;
	statusLabel: EmergencyCareStatusLabels;
	attentionCreator: RegisterEditor;
	_episode: Episode;

	@Input() set episode(episode: Episode) {
		this._episode = episode;
		this.statusLabel = { stateId: this._episode.state.id, description: "guardia.home.episodes.episode.status.IN_ATTETION" }
		this.attentionCreator = { createdBy: `${episode.relatedProfessional.firstName} ${episode.relatedProfessional.lastName}` };
	};
	constructor() { }

}
