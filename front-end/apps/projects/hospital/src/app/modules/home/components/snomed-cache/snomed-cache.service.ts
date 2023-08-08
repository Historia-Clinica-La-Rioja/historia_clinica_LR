import { Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest } from 'rxjs';

import { SnomedECL, TerminologyCSVDto, TerminologyQueueItemDto } from '@api-rest/api-model';
import { TerminologyCacheService } from '@api-rest/services/terminology-cache.service';

@Injectable({
	providedIn: 'root'
})
export class SnomedCacheService {
	status$ = new BehaviorSubject<TerminologyECLStatus[]>(undefined);

	constructor(
		private terminologyCacheService: TerminologyCacheService,
	) { }

	fetch() {

		combineLatest([
			this.terminologyCacheService.getStatus(),
			this.terminologyCacheService.getQueue(),
		]).subscribe(([
			statusList,
			queueList,
		])=> {
			this.status$.next(statusList.map(eclStatus => ({
				...eclStatus,
				queue: queueList.filter(q => q.ecl === eclStatus.ecl)
			})).sort((a, b) => a.ecl > b.ecl ? 1 : -1));
		});

	}

	add(newCsv: TerminologyCSVDto) {
		this.terminologyCacheService.addCsv(newCsv).subscribe(
			_ => this.fetch()
		);;
	}

	delete(terminologyId: number) {
		this.terminologyCacheService.delete(terminologyId).subscribe(
			_ => this.fetch()
		)
	}

}

export interface TerminologyECLStatus {
    ecl: SnomedECL;
    successful?: TerminologyQueueItemDto;
	queue?: TerminologyQueueItemDto[];
	updating?: boolean;
	loadStatus?: LoadStatus;
	isPanelOpened?: boolean;
}

export interface LoadStatus {
	id: number,
	description: string,
	color: string
}
