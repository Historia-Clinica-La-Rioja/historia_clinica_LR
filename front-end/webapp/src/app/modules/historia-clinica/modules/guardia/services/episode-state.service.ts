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

	atender(episodeId: number, doctorsOfficeId: number): Observable<boolean> {
		return this.emergencyCareEpisodeStateService
			.changeState(EstadosEpisodio.EN_ATENCION, episodeId, doctorsOfficeId);
	}

	finalizarPorAusencia(episodeId: number): Observable<boolean> {
		return this.emergencyCareEpisodeStateService
			.changeState(EstadosEpisodio.FINALIZADO_POR_AUSENCIA, episodeId);
	}
}
