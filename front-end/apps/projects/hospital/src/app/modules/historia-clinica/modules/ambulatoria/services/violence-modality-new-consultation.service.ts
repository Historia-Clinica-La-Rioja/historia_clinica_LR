import { Injectable } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { pushIfNotExists } from '@core/utils/array.utils';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  	providedIn: 'root'
})
export class ViolenceModalityNewConsultationService {

	violenceModalities$ = new BehaviorSubject<SnomedDto[]>([]);
	violenceModalities: SnomedDto[] = [];

	addToList(concept: SnomedDto) {
		this.violenceModalities = pushIfNotExists<any>(this.violenceModalities, concept, this.compareConcept);
		this.violenceModalities$.next(this.violenceModalities);
	}

	compareConcept(data: SnomedDto, data1: SnomedDto): boolean {
		return data.sctid === data1.sctid;
	}

	removeViolenceModality(index: number) {
		this.violenceModalities.splice(index, 1);
		this.violenceModalities$.next(this.violenceModalities);
	}

	reset(){
		this.violenceModalities = [];
		this.violenceModalities$.next(this.violenceModalities);
	}
}
