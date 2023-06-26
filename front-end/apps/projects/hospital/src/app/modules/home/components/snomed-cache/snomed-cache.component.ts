import { Component, OnInit } from '@angular/core';
import { SnomedCacheService } from './snomed-cache.service';
import { TerminologyCSVDto, TerminologyQueueItemDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { DatePipeFormat } from '@core/utils/date.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { TranslateService } from '@ngx-translate/core';


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
		private readonly snackBarService: SnackBarService,
		private readonly translateService: TranslateService
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

	refresh() {
		this.translateService.get("configuracion.snomed-cache.DATA_UPDATED").subscribe(
			translatedText => this.snackBarService.showSuccess(translatedText)
		);
		this.list();
	}

	delete(terminologyId: number) {
		this.snomedCacheService.delete(terminologyId);
	}

}
