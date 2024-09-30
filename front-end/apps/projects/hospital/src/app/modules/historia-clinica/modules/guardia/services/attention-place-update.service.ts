import { Injectable } from '@angular/core';
import { ChangeEmergencyCareEpisodeAttentionPlaceDto } from '@api-rest/api-model';
import { Observable, Subject } from 'rxjs';

@Injectable()
export class AttentionPlaceUpdateService {
	private updateSubject = new Subject<ChangeEmergencyCareEpisodeAttentionPlaceDto>();

	get update$(): Observable<ChangeEmergencyCareEpisodeAttentionPlaceDto> {
	  return this.updateSubject.asObservable();
	}

	notifyUpdate(newAttentionPlace: ChangeEmergencyCareEpisodeAttentionPlaceDto) {
	  this.updateSubject.next(newAttentionPlace);
	}
}
