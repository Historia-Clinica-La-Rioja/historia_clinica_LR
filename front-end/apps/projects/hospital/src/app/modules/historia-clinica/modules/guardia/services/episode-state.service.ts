import {Injectable} from '@angular/core';
import {EmergencyCareEpisodeStateService} from '@api-rest/services/emergency-care-episode-state.service';
import {EstadosEpisodio} from '../constants/masterdata';
import {Observable} from 'rxjs';
import { EmergencyCareEpisodeAttentionPlaceDto } from '@api-rest/api-model';

@Injectable()
export class EpisodeStateService {

	constructor(
		readonly emergencyCareEpisodeStateService: EmergencyCareEpisodeStateService
	) {
	}

	call(episodeId: number, attentionPlaceDto?: EmergencyCareEpisodeAttentionPlaceDto): Observable<boolean> {
		const toState = 'call';
		return this.emergencyCareEpisodeStateService
		.changeToSpecificState(episodeId, toState, attentionPlaceDto);
	}

	attend(episodeId: number, attentionPlaceDto?: EmergencyCareEpisodeAttentionPlaceDto): Observable<boolean> {
		const toState = 'attend';
		return this.emergencyCareEpisodeStateService
		   .changeToSpecificState(episodeId, toState, attentionPlaceDto);
	}

	pasarAEspera(episodeId: number): Observable<boolean> {
		return this.emergencyCareEpisodeStateService
			.changeState(episodeId, EstadosEpisodio.EN_ESPERA);
	}

	markAsAbsent(episodeId: number): Observable<boolean> {
		const toState = 'absent';
		return this.emergencyCareEpisodeStateService
		.changeToSpecificState(episodeId, toState);
	}

}
