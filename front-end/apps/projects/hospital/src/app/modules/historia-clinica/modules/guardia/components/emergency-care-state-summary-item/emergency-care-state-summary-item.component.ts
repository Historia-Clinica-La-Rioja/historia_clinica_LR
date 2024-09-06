import { Component, Input } from '@angular/core';
import { Episode } from '../emergency-care-patients-summary/emergency-care-patients-summary.component';
import { EstadosEpisodio } from '../../constants/masterdata';

@Component({
	selector: 'app-emergency-care-state-summary-item',
	templateUrl: './emergency-care-state-summary-item.component.html',
	styleUrls: ['./emergency-care-state-summary-item.component.scss']
})
export class EmergencyCareStateSummaryItemComponent {

	readonly EPISODE_STATES = EstadosEpisodio;
	@Input() episode: Episode;

	constructor() { }


}
