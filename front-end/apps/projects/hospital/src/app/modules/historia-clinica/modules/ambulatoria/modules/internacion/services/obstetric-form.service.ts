import { Injectable } from '@angular/core';
import { ObstetricEventDto } from '@api-rest/api-model';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()

export class ObstetricFormService {
	arrayNewborn = [];

	obstetricEvent = new BehaviorSubject<ObstetricEventDto>(null);
	private obstetricSubject = new BehaviorSubject<boolean>(true);
	set obstetric(obstetric: ObstetricEventDto) {
		const { currentPregnancyEndDate, gestationalAge, newborns, pregnancyTerminationType, previousPregnancies } = obstetric;
		const isObstetricEmpty = !(currentPregnancyEndDate || gestationalAge || newborns?.length > 0 || pregnancyTerminationType || previousPregnancies);
		this.obstetricSubject.next(isObstetricEmpty);
	}

	setValue(obstetricEvent: ObstetricEventDto) {
		this.obstetric = obstetricEvent;
		this.arrayNewborn = obstetricEvent.newborns;
		this.obstetricEvent.next(obstetricEvent);
	}

	getValue(): Observable<ObstetricEventDto> {
		return this.obstetricEvent.asObservable();
	}

	isEmptyObstetric(): Observable<boolean> {
		return this.obstetricSubject.asObservable();
	}


}
