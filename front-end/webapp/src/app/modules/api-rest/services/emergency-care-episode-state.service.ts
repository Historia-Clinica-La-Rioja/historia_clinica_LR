import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEpisodeStateService {

	constructor() {
	}

	changeState(emergencyCareEpisodeStateId: number, episodeId: number, doctorsOfficeId?: number): Observable<boolean> {
		return of(randomBoolean());

		function randomBoolean(): boolean {
			return Math.random() < 0.5;
		}
	}

}
