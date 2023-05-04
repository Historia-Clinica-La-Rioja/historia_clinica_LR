import { Injectable } from '@angular/core';
import { EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';
import { Subject } from 'rxjs';

@Injectable({
  	providedIn: 'root'
})
export class EmergencyCareStateChangedService {

	readonly emergencyCareStateChangedEmitter = new Subject<EstadosEpisodio>();
	emergencyCareStateChanged$ = this.emergencyCareStateChangedEmitter.asObservable();

	constructor() { }

	emergencyCareStateChanged(emergencyCareEpisodeStateid: EstadosEpisodio) {
		this.emergencyCareStateChangedEmitter.next(emergencyCareEpisodeStateid);
	}

}
