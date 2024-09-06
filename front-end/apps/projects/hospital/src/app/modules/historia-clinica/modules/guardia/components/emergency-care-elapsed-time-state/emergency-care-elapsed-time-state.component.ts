import { Component, Input } from '@angular/core';
import { DateTimeDto } from '@api-rest/api-model';
import { EmergencyCareStatusLabels } from '@hsi-components/emergency-care-status-labels/emergency-care-status-labels.component';
import { Episode } from '../emergency-care-patients-summary/emergency-care-patients-summary.component';
import { EstadosEpisodio } from '../../constants/masterdata';
import { TranslateService } from '@ngx-translate/core';

@Component({
	selector: 'app-emergency-care-elapsed-time-state',
	templateUrl: './emergency-care-elapsed-time-state.component.html',
	styleUrls: ['./emergency-care-elapsed-time-state.component.scss']
})
export class EmergencyCareElapsedTimeStateComponent {

	readonly CALL = EstadosEpisodio.LLAMADO;
	stateUpdatedOn: DateTimeDto;
	statusLabel: EmergencyCareStatusLabels;

	@Input() set episode(episode: Episode) {
		this.stateUpdatedOn = episode.stateUpdatedOn;
		this.statusLabel = { stateId: episode.state.id, description: this.getDescription(episode.state.id, episode.calls) };
	};

	constructor(
		private readonly translateService: TranslateService,
	) { }

	private getDescription(stateId: number, calls: number): string {
		const descriptionByState = {
			[EstadosEpisodio.EN_ESPERA]: 'guardia.home.episodes.episode.status.WAITING',
			[EstadosEpisodio.AUSENTE]: 'guardia.home.episodes.episode.status.ABSENT',
			[EstadosEpisodio.LLAMADO]: this.translateService.instant('guardia.home.episodes.episode.status.CALLED', {nro: calls})
		}

		return descriptionByState[stateId];

	}

}
