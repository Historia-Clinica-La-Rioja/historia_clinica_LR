import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Episode } from '../routes/home/home.component';
import { TriageCategoryDto, TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { Observable } from 'rxjs';
import { MasterDataInterface } from '@api-rest/api-model';
import { tap } from 'rxjs/operators';
import { atLeastOneValueInFormGroup } from '@core/utils/form.utils';
import { PERSON, REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';

const NO_INFO: MasterDataInterface<number> = {
	id: -1,
	description: 'No definido'
};

export class EpisodeFilterService {

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly triageMasterDataService: TriageMasterDataService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService
	) {
		this.form = this.formBuilder.group({
			triage: [null],
			emergencyCareType: [null],
			patientId: [null],
			identificationNumber: [null, Validators.maxLength(PERSON.MAX_LENGTH.identificationNumber)],
			firstName: [null, Validators.maxLength(PERSON.MAX_LENGTH.firstName)],
			lastName: [null, Validators.maxLength(PERSON.MAX_LENGTH.lastName)],
			temporal: [null],
			noPatient: [null]
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

	static filterByPatientId(episode: Episode, filters: EpisodeFilters): boolean {
		return (filters.patientId ? episode.patient?.id === filters.patientId : true);
	}

	static filterByIdentificationNumber(episode: Episode, filters: EpisodeFilters): boolean {
		const identificationNumberWithoutZeros = Number(filters.identificationNumber?.replace(REMOVE_SUBSTRING_DNI, '')).toString();
		return (filters.identificationNumber ?
			this.filterString(episode.patient?.person?.identificationNumber, identificationNumberWithoutZeros) : true);
	}

	static filterByFirstName(episode: Episode, filters: EpisodeFilters): boolean {
		return (filters.firstName ?
			this.filterString(episode.patient?.person?.firstName, filters.firstName) : true);
	}

	static filterByLastName(episode: Episode, filters: EpisodeFilters): boolean {
		return (filters.lastName ?
			this.filterString(episode.patient?.person?.lastName, filters.lastName) : true);
	}

	static filterString(target: string, filterValue: string): boolean {
		return target?.toLowerCase().includes(filterValue.toLowerCase());
	}

	static filterTemporal(episode: Episode, filters: EpisodeFilters): boolean {
		const PACIENTE_TEMPORAL = 3;
		return (filters.temporal ? episode.patient?.typeId === PACIENTE_TEMPORAL : true);
	}

	static filterNoPatient(episode: Episode, filters: EpisodeFilters) {
		return (filters.noPatient ? !episode.patient : true);
	}

	getForm(): FormGroup {
		return this.form;
	}

	filter(episode: Episode): boolean {
		const filters = this.form.value as EpisodeFilters;
		return 	EpisodeFilterService.filterByTriage(episode, filters) &&
				EpisodeFilterService.filterByEmergencyCareType(episode, filters) &&
				EpisodeFilterService.filterByIdentificationNumber(episode, filters) &&
				EpisodeFilterService.filterByPatientId(episode, filters) &&
				EpisodeFilterService.filterByFirstName(episode, filters) &&
				EpisodeFilterService.filterByLastName(episode, filters) &&
				EpisodeFilterService.filterTemporal(episode, filters) &&
				EpisodeFilterService.filterNoPatient(episode, filters);
	}

	clear(control: string): void {
		this.form.controls[control].reset();
	}

	markAsFiltered(): void {
		this.form.markAllAsTouched();
	}

	hasFilters(): boolean {
		return atLeastOneValueInFormGroup(this.form);
	}

	isValid(): boolean {
		return this.form.valid;
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
	patientId?: number;
	identificationNumber?: string;
	firstName?: string;
	lastName?: string;
	temporal?: boolean;
	noPatient?: boolean;
}
