import { Injectable } from "@angular/core";
import { DocumentHistoricDto, DocumentSearchFilterDto } from "@api-rest/api-model";
import { DocumentSearchService } from "@api-rest/services/document-search.service";
import { Subject } from "rxjs";


@Injectable({
	providedIn: 'root'
})
export class EvolutionNotesListenerService {

	private subject = new Subject<DocumentHistoricDto>();
	public searchFilter: DocumentSearchFilterDto;
	public internmentEpisodeId: number;

	history$ = this.subject.asObservable();

	constructor(private documentSearchService: DocumentSearchService) { }

	private loadHistoric(): void {
		this.documentSearchService.getHistoric(this.internmentEpisodeId, this.searchFilter)
		.subscribe(historicalData => {
			this.subject.next(historicalData);
		});
	}

	public setSerchFilter(searchFilter: DocumentSearchFilterDto) {
		this.searchFilter = searchFilter;
		this.loadHistoric();
	}

	public loadEvolutionNotes() {
		this.loadHistoric();
	}

	initializeEvolutionNoteFilterResult(internmentEpisodeId: number) {
		this.internmentEpisodeId = internmentEpisodeId;
		this.setSerchFilter(null);
	}
}
