import {Injectable} from '@angular/core';
import {EmergencyCareEpisodeStateService} from '@api-rest/services/emergency-care-episode-state.service';
import {EstadosEpisodio} from '../constants/masterdata';
import {Observable} from 'rxjs';

@Injectable()
export class EpisodeStateService {

	constructor(
		readonly emergencyCareEpisodeStateService: EmergencyCareEpisodeStateService
	) {
	}

	atender(episodeId: number, doctorsOfficeId: number, shockroomId?: number, bedId?: number): Observable<boolean> {
		return this.emergencyCareEpisodeStateService
			.changeState(episodeId, EstadosEpisodio.EN_ATENCION, doctorsOfficeId, shockroomId, bedId);
	}

	pasarAEspera(episodeId: number): Observable<boolean> {
		return this.emergencyCareEpisodeStateService
			.changeState(episodeId, EstadosEpisodio.EN_ESPERA);
	}

	markAsAbsent(episodeId: number): Observable<boolean> {
		const toState = 'absent';
		return this.emergencyCareEpisodeStateService
			.changeToSpecificState(episodeId, EstadosEpisodio.AUSENTE, toState);
	}

}
