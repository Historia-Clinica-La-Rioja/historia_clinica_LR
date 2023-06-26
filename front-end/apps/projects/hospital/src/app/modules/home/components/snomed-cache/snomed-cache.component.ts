import { Component, OnInit } from '@angular/core';
import { SnomedCacheService } from './snomed-cache.service';
import { TerminologyCSVDto, TerminologyQueueItemDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { DatePipeFormat } from '@core/utils/date.utils';


@Component({
	selector: 'app-snomed-cache',
	templateUrl: './snomed-cache.component.html',
	styleUrls: ['./snomed-cache.component.scss']
})
export class SnomedCacheComponent implements OnInit {
	queue$: Observable<TerminologyQueueItemDto[]>;
	readonly dateFormats = DatePipeFormat;

	constructor(
		private snomedCacheService: SnomedCacheService,
	) { }

	ngOnInit(): void {
		this.queue$ = this.snomedCacheService.queue$.asObservable();
		this.list();
	}

	addCsv(newCsv: TerminologyCSVDto) {
		this.snomedCacheService.add(newCsv);
	}

	list() {
		this.snomedCacheService.list();
	}

	delete(terminologyId: number) {
		this.snomedCacheService.delete(terminologyId);
	}

}
