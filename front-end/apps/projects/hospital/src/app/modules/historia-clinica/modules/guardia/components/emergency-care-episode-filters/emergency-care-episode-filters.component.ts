import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { EpisodeFilterService } from '../../services/episode-filter.service';
import { TriageCategory } from '../triage-chip/triage-chip.component';
import { Observable } from 'rxjs';
import { MasterDataInterface } from '@api-rest/api-model';
import { getError, hasError } from '@core/utils/form.utils';

@Component({
	selector: 'app-emergency-care-episode-filters',
	templateUrl: './emergency-care-episode-filters.component.html',
	styleUrls: ['./emergency-care-episode-filters.component.scss']
})
export class EmergencyCareEpisodeFiltersComponent implements OnInit {

	readonly getError = getError;
	readonly hasError = hasError;

	triageCategories$: Observable<TriageCategory[]>;
	emergencyCareTypes$: Observable<MasterDataInterface<number>[]>;
	@Output() updateEpisodes = new EventEmitter<void>();

	constructor(
		readonly filterService: EpisodeFilterService,
	) { }

	ngOnInit() {
		this.triageCategories$ = this.filterService.getTriageCategories();
		this.emergencyCareTypes$ = this.filterService.getEmergencyCareTypes();
	}

	filter() {
		this.filterService.markAsFiltered();
		if (this.filterService.isValid()) {
			this.updateEpisodes.emit();
		}
	}

	clear(control: string) {
		this.filterService.clear(control);
		this.filter();
	}

}
