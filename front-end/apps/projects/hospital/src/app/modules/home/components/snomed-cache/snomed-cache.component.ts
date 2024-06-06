import { Component, Input, OnInit } from '@angular/core';
import { LoadStatus, SnomedCacheService, TerminologyECLStatus } from './snomed-cache.service';
import { ETerminologyKind, SnomedECL, TerminologyQueueItemDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { TranslateService } from '@ngx-translate/core';
import { Color } from '@presentation/colored-label/colored-label.component';
import { TerminologyCacheService } from '@api-rest/services/terminology-cache.service';

export const TERMINOLOGYLOADSTATUS = {
	LOADED: {
		id: 1,
		description: 'Cargado',
		color: Color.GREEN
	},
	NOT_LOADED: {
		id: 2,
		description: 'No cargado',
		color: Color.RED
	},
	PENDING: {
		id: 3,
		description: 'Pendiente',
		color: Color.YELLOW
	}
}

@Component({
	selector: 'app-snomed-cache',
	templateUrl: './snomed-cache.component.html',
	styleUrls: ['./snomed-cache.component.scss']
})
export class SnomedCacheComponent implements OnInit {
	@Input() kind: ETerminologyKind;
	terminologies: TerminologyECLStatus[];
	queue$: Observable<TerminologyQueueItemDto[]>;
	disableForm = false;
	terminologyLoadStatus = TERMINOLOGYLOADSTATUS;
	private snomedCacheService: SnomedCacheService;

	constructor(
		private terminologyCacheService: TerminologyCacheService,
		private readonly snackBarService: SnackBarService,
		private readonly translateService: TranslateService
	) {
	}

	ngOnInit(): void {
		this.snomedCacheService = new SnomedCacheService(
			this.terminologyCacheService,
			this.kind,
		);
		this.list();
		this.snomedCacheService.status$.asObservable().subscribe(terminologies => {
			this.terminologies = terminologies;
			this.terminologies?.forEach(term => {
				term.loadStatus = this.checkForStatus(term);
				term.updating = this.checkQueueStatus(term);
				term.isPanelOpened = false;
			})
		})
	}

	private checkForStatus(term: TerminologyECLStatus): LoadStatus {
		if (term.successful) return TERMINOLOGYLOADSTATUS.LOADED;
		return TERMINOLOGYLOADSTATUS.NOT_LOADED;
	}

	updatePanelStatus(term: TerminologyECLStatus, isPanelOpened: boolean) {
		term.isPanelOpened = isPanelOpened;
	}

	checkQueueStatus(terminology: TerminologyECLStatus): boolean{
		let isUpdating = false;
		if (terminology.queue?.length) {
			terminology.queue.map(term => {
				if ((term.createdOn && !term.ingestedOn) && !term.downloadedError) isUpdating = true;
			})

		}
		return isUpdating;
	}

	addCsv(ecl: SnomedECL, url: string) {
		this.snomedCacheService.add(ecl, url);
	}

	list() {
		this.snomedCacheService.fetch();
	}

	refresh() {
		this.translateService.get("configuracion.snomed-cache.DATA_UPDATED").subscribe(
			translatedText => this.snackBarService.showSuccess(translatedText)
		);
		this.list();
	}

	delete(tqi: TerminologyQueueItemDto) {
		this.snomedCacheService.delete(tqi.id)
	}
}
