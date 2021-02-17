import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Episode } from '../routes/home/home.component';
import { TriageCategoryDto, TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { Observable } from 'rxjs';
import { MasterDataInterface } from '@api-rest/api-model';
import { tap } from 'rxjs/operators';
import { atLeastOneValueInFormGroup } from '@core/utils/form.utils';

const NO_INFO: MasterDataInterface<number> = {
	id: -1,
	description: 'No definido'
};

@Injectable({
	providedIn: 'root'
})
export class EpisodesFilterService {

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly triageMasterDataService: TriageMasterDataService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService
	) {
		this.form = this.formBuilder.group({
			triage: [null],
			emergencyCareType: [null]
		});
	}

	private form: FormGroup;

	static filterByTriage(episode: Episode, filters: EpisodeFilters): boolean {
		return (filters.triage ? episode.triage.id === filters.triage : true);
	}

	static filterByEmergencyCareType(episode: Episode, filters: EpisodeFilters): boolean {
		if (!filters.emergencyCareType) {
			return true;
		}
		if (filters.emergencyCareType === NO_INFO.id) {
			return (!episode.type);
		}
		return (filters.emergencyCareType ? episode.type?.id === filters.emergencyCareType : true);
	}

	getForm(): FormGroup {
		return this.form;
	}

	filter(episodes: Episode[]): Episode[] {
		const filters = this.form.value as EpisodeFilters;
		return episodes.filter(episode => {
			return EpisodesFilterService.filterByTriage(episode, filters) &&
				EpisodesFilterService.filterByEmergencyCareType(episode, filters);
		});
	}

	clear(control: string): void {
		this.form.controls[control].reset();
	}

	hasFilters(): boolean {
		return atLeastOneValueInFormGroup(this.form);
	}

	getTriageCategories(): Observable<TriageCategoryDto[]> {
		return this.triageMasterDataService.getCategories();
	}

	getEmergencyCareTypes(): Observable<MasterDataInterface<number>[]> {
		return this.emergencyCareMasterDataService.getType().pipe(tap(types => types.unshift(NO_INFO)));
	}

}

interface EpisodeFilters {
	triage?: number;
	emergencyCareType?: number;
}
