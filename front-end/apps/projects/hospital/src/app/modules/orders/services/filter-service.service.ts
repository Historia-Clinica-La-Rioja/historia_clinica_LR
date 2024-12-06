import { Injectable } from '@angular/core';
import { StudyOrderWorkListFilterDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class FilterServiceService {

	filterSubject = new BehaviorSubject<StudyOrderWorkListFilterDto>({
		categories: ["108252007"],
		notRequiresTransfer: false,
		patientTypeId: [1, 2, 3, 4, 5, 6, 7, 8],
		requiresTransfer: false,
		sourceTypeIds: [],
		studyTypeIds: [],
	});

	readonly filters$ = this.filterSubject.asObservable();

	updateFilters(newFilters: Partial<StudyOrderWorkListFilterDto>) {
		const currentFilters = this.filterSubject.value;
		this.filterSubject.next({ ...currentFilters, ...newFilters });
	}
}
