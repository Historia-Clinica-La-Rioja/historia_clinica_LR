import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { map, Observable } from 'rxjs';
import { EmergencyCareClinicalSpecialtySectorDto, EmergencyCareEpisodeFilterDto, MasterDataDto, MasterDataInterface, TriageCategoryDto } from '@api-rest/api-model';
import { Injectable } from '@angular/core';
import { FormCheckbox, FormChips } from '../components/emergency-care-episode-filters/emergency-care-episode-filters.component';
import { EstadosEpisodio } from '../constants/masterdata';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';

const TRIAGE_CATEGORIES_FORM = 'triageCategories';
const EMERGENCY_CARE_TYPES_FORM = 'emergencyCareTypes';
const STATES_FORM = 'states';

@Injectable({
	providedIn: 'root'
})
export class EpisodeFilterService {

	filters: EmergencyCareEpisodeFilterDto;
	hasSelectedFilters = false;

	constructor(
		private readonly triageMasterDataService: TriageMasterDataService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
	) {
		this.initializeFilters();
	}

	getCategories(): Observable<TriageCategoryDto[]> {
		return this.triageMasterDataService.getCategories();
	}

	getFilterValue(filterKey: string): any {
		return this.filters?.[filterKey] || null;
	}

	getEmergencyCareTypes(): Observable<MasterDataInterface<number>[]> {
		return this.emergencyCareMasterDataService.getType();
	}

	getStates(): Observable<MasterDataDto[]> {
		return this.emergencyCareMasterDataService.getEmergencyCareStates().pipe(map(states => states.filter(state => state.id != EstadosEpisodio.CON_ALTA_ADMINISTRATIVA)))
	}

	getServices(): Observable<EmergencyCareClinicalSpecialtySectorDto[]> {
		return this.emergencyCareEpisodeService.getSpecialtySectors();
	}

	setFilters(filters: EpisodeFilters) {
		this.filters = {
			...this.filters,
			clinicalSpecialtySectorIds: filters.clinicalSpecialtySectorIds ? this.getClinicalSpecialtySectorIds(filters.clinicalSpecialtySectorIds) : this.filters.clinicalSpecialtySectorIds,
			identificationNumber: filters.identificationNumber,
			mustBeEmergencyCareTemporal: filters.emergencyCareTemporary,
			patientFirstName: filters.firstName,
			patientId: filters.patientId,
			patientLastName: filters.lastName,
			stateIds: filters.states ? this.getCheckboxFilterValue(filters.states, STATES_FORM) : this.filters.stateIds,
			triageCategoryIds: filters.triageCategories ? this.getCheckboxFilterValue(filters.triageCategories, TRIAGE_CATEGORIES_FORM) : this.filters.triageCategoryIds,
			typeIds: filters.emergencyCareTypes ? this.getCheckboxFilterValue(filters.emergencyCareTypes, EMERGENCY_CARE_TYPES_FORM) : this.filters.typeIds,
		};

		this.calculateSelectedFilters();
	}

	resetFilters() {
		this.initializeFilters();
	}

	private getCheckboxFilterValue(formValues: FormCheckbox, controlName: string): number[] {
		if (!formValues) return [];

		const values = formValues[controlName];
		if (!values) return [];
		let selectedCategories: number[] = [];
		Object.entries(values).forEach(([key, value]) => {
			if (value)
				selectedCategories.push(Number(key));
		});

		return selectedCategories;
	}

	private calculateSelectedFilters() {
		this.hasSelectedFilters = !!(this.filters.clinicalSpecialtySectorIds?.length || this.filters.identificationNumber || this.filters.mustBeEmergencyCareTemporal || this.filters.patientFirstName || this.filters.patientId || this.filters.patientLastName || this.filters.stateIds?.length || this.filters.triageCategoryIds?.length || this.filters.typeIds?.length)
	}

	private getClinicalSpecialtySectorIds(formValue: FormChips): number[] {
		return formValue?.clinicalSpecialtySectorIds ? formValue.clinicalSpecialtySectorIds : [];
	}

	private initializeFilters() {
		this.filters = {
			clinicalSpecialtySectorIds: [],
			identificationNumber: null,
			mustBeEmergencyCareTemporal: false,
			patientFirstName: null,
			patientId: null,
			patientLastName: null,
			stateIds: [],
			triageCategoryIds: [],
			typeIds: []
		}
	}

}

export interface EpisodeFilters {
	triageCategories?: FormCheckbox;
	emergencyCareTypes?: FormCheckbox;
	states?: FormCheckbox;
	clinicalSpecialtySectorIds?: FormChips;
	patientId?: number;
	identificationNumber?: string;
	firstName?: string;
	lastName?: string;
	emergencyCareTemporary?: boolean;
	administrativeDischarge?: boolean;
}
