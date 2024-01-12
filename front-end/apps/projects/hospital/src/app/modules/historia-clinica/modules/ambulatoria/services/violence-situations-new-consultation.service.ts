import { Injectable } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { pushIfNotExists } from '@core/utils/array.utils';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  	providedIn: 'root'
})
export class ViolenceSituationsNewConsultationService {

	violenceSituations$ = new BehaviorSubject<SnomedDto[]>([]);
	violenceSituations: SnomedDto[] = [];

	addToList(concept: SnomedDto) {
		this.violenceSituations = pushIfNotExists<any>(this.violenceSituations, concept, this.compareConcept);
		this.violenceSituations$.next(this.violenceSituations);
	}

	compareConcept(data: SnomedDto, data1: SnomedDto): boolean {
		return data.sctid === data1.sctid;
	}

	removeViolenceSituation(index: number) {
		this.violenceSituations.splice(index, 1);
		this.violenceSituations$.next(this.violenceSituations);
	}

	reset(){
		this.violenceSituations = [];
		this.violenceSituations$.next(this.violenceSituations);
	}
}
