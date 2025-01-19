import { BehaviorSubject, combineLatest } from 'rxjs';

import { ETerminologyKind, SnomedECL, TerminologyQueueItemDto } from '@api-rest/api-model';
import { TerminologyCacheService } from '@api-rest/services/terminology-cache.service';

export class SnomedCacheService {
	status$ = new BehaviorSubject<TerminologyECLStatus[]>(undefined);

	constructor(
		private terminologyCacheService: TerminologyCacheService,
		private kind: ETerminologyKind,
	) { }

	fetch() {
		combineLatest([
			this.terminologyCacheService.getStatus(this.kind),
			this.terminologyCacheService.getQueue(this.kind),
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

	add(ecl: SnomedECL, url: string) {
		this.terminologyCacheService.addCsv({
			ecl,
			url,
			kind: this.kind,
		}).subscribe(
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
