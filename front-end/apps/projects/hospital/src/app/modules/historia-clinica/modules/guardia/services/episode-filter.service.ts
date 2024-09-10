import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { map, Observable } from 'rxjs';
import { EmergencyCareEpisodeFilterDto, MasterDataDto, MasterDataInterface, TriageCategoryDto } from '@api-rest/api-model';
import { Injectable } from '@angular/core';
import { FormCheckbox } from '../components/emergency-care-episode-filters/emergency-care-episode-filters.component';
import { EstadosEpisodio } from '../constants/masterdata';

const TRIAGE_CATEGORIES_FORM = 'triageCategories';
const EMERGENCY_CARE_TYPES_FORM = 'emergencyCareTypes';
const STATES_FORM = 'states';

@Injectable()
export class EpisodeFilterService {

	filters: EmergencyCareEpisodeFilterDto;
	hasSelectedFilters = false;

	constructor(
		private readonly triageMasterDataService: TriageMasterDataService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService
	) {
		this.initializeFilters();
	}

	getCategories(): Observable<TriageCategoryDto[]> {
		return this.triageMasterDataService.getCategories();
	}

	getEmergencyCareTypes(): Observable<MasterDataInterface<number>[]> {
		return this.emergencyCareMasterDataService.getType();
	}

	getStates(): Observable<MasterDataDto[]> {
		return this.emergencyCareMasterDataService.getEmergencyCareStates().pipe(map(states => states.filter(state => state.id != EstadosEpisodio.CON_ALTA_ADMINISTRATIVA)))
	}

	setFilters(filters: EpisodeFilters) {
		this.filters = {
			clinicalSpecialtySectorIds: [],
			identificationNumber: filters.identificationNumber,
			mustBeEmergencyCareTemporal: filters.emergencyCareTemporary,
			patientFirstName: filters.firstName,
			patientId: filters.patientId,
			patientLastName: filters.lastName,
			stateIds: this.getCheckboxFilterValue(filters.states, STATES_FORM),
			triageCategoryIds: this.getCheckboxFilterValue(filters.triageCategories, TRIAGE_CATEGORIES_FORM),
			typeIds: this.getCheckboxFilterValue(filters.emergencyCareTypes, EMERGENCY_CARE_TYPES_FORM),
		}
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
	patientId?: number;
	identificationNumber?: string;
	firstName?: string;
	lastName?: string;
	emergencyCareTemporary?: boolean;
	administrativeDischarge?: boolean;
}
