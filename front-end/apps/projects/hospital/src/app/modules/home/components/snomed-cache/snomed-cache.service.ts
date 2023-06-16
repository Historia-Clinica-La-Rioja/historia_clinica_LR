import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

import { TerminologyCSVDto, TerminologyQueueItemDto } from '@api-rest/api-model';
import { TerminologyCacheService } from '@api-rest/services/terminology-cache.service';

@Injectable({
	providedIn: 'root'
})
export class SnomedCacheService {
	queue$ = new BehaviorSubject<TerminologyQueueItemDto[]>(undefined);
	constructor(
		private terminologyCacheService: TerminologyCacheService,
	) { }

	list() {
		this.terminologyCacheService.getQueue().subscribe(
			list => this.queue$.next(list)
		);
	}

	add(newCsv: TerminologyCSVDto) {
		this.terminologyCacheService.addCsv(newCsv).subscribe(
			_ => this.list()
		);;
	}

}
