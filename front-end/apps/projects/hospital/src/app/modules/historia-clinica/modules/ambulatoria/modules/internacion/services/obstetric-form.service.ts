import { Injectable } from '@angular/core';
import { ObstetricEventDto } from '@api-rest/api-model';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()

export class ObstetricFormService {
	arrayNewborn = [];

	obstetricEvent = new BehaviorSubject<ObstetricEventDto>(null);

	setValue(obstetricEvent: ObstetricEventDto) {
		this.arrayNewborn = obstetricEvent.newborns;
		this.obstetricEvent.next(obstetricEvent);
	}

	getValue(): Observable<ObstetricEventDto> {
		return this.obstetricEvent.asObservable();
	}
}
