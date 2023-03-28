import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SnomedECL } from '@api-rest/api-model';
import { SnomedDto } from '@api-rest/api-model';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { BasicTable } from '@material/model/table.model';
import { Observable } from 'rxjs';
import { ExternalCauseService } from '../../services/external-cause.service';

@Component({
	selector: 'app-event-serch',
	templateUrl: './event-serch.component.html',
	styleUrls: ['./event-serch.component.scss']
})
export class EventSerchComponent implements OnInit {
	ECL = SnomedECL.EVENT;

	table$: Observable<BasicTable<SnomedDto>>;
	@Input() searchConceptsLocallyFF = false;
	@Output() eventSelected = new EventEmitter<SnomedDto>();
	constructor(
		readonly externalCauseServise: ExternalCauseService,
		private readonly snomedService: SnomedService,

	) { }

	ngOnInit() {
		this.table$ = this.externalCauseServise.table$;
	}

	openSearchDialogEvent(searchValue: string) {

		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.EVENT
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => {

					this.externalCauseServise.setConceptEventSnomed(selectedConcept); this.eventSelected.emit(selectedConcept);
					console.log("event serch selectedConcept", selectedConcept);
				});
		}
	}

	emmitEvent($event: any) {
		this.externalCauseServise.setConceptEventSnomed($event);
		this.eventSelected.emit($event);
	}
}
