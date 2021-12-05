import { Injectable } from '@angular/core';
import { ExternalClinicalHistoryDto } from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { ExternalClinicalHistoryService } from '@api-rest/services/external-clinical-history.service';
import { dateToMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { Observable, ReplaySubject } from 'rxjs';
import { ExternalClinicalHistoryFilter, ExternalClinicalHistoryFiltersOptions } from '../components/external-clinical-histories-filters/external-clinical-histories-filters.component';

@Injectable({
	providedIn: 'root'
})
export class ExternalClinicalHistoryFacadeService {

	private externalClinicalHistoryList: ExternalClinicalHistoryDto[] = [];
	private hasInformationSubject: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
	private hasInfo$: Observable<boolean>;

	private filtersOptions: ExternalClinicalHistoryFiltersOptions;
	private filtersOptionsSubject: ReplaySubject<ExternalClinicalHistoryFiltersOptions> = new ReplaySubject<ExternalClinicalHistoryFiltersOptions>(1);
	private filtersOptions$: Observable<ExternalClinicalHistoryFiltersOptions>;

	private filters: ExternalClinicalHistoryFilter = {};
	private filteredHistories: ExternalClinicalHistoryDto[] = [];
	private filteredHistoriesSubject: ReplaySubject<ExternalClinicalHistoryDto[]> = new ReplaySubject<ExternalClinicalHistoryDto[]>(1);
	private filteredHistories$: Observable<ExternalClinicalHistoryDto[]>;

	constructor(private readonly externalClinicalHistoryService: ExternalClinicalHistoryService) {
		this.filteredHistories$ = this.filteredHistoriesSubject.asObservable();
		this.filtersOptions$ = this.filtersOptionsSubject.asObservable();
		this.hasInfo$ = this.hasInformationSubject.asObservable();
	}

	// It's necessary to invoke this method to bring the information from Backend
	public loadInformation(patientId: number): void {
		this.externalClinicalHistoryService.getExternalClinicalHistoryList(patientId).subscribe(
			(externalClinicalHistories: ExternalClinicalHistoryDto[]) => {
				this.externalClinicalHistoryList = externalClinicalHistories;
				this.hasInformationSubject.next(externalClinicalHistories.length > 0);
				this.filteredHistories = externalClinicalHistories;
				this.filteredHistoriesSubject.next(externalClinicalHistories);
				this.filtersOptions = this.loadFiltersOptions();
				this.filtersOptionsSubject.next(this.filtersOptions);
			}
		);
	}

	public getFilteredHistories$(): Observable<ExternalClinicalHistoryDto[]> {
		return this.filteredHistories$;
	}

	public getFiltersOptions$(): Observable<ExternalClinicalHistoryFiltersOptions> {
		return this.filtersOptions$;
	}

	public setFilters(filters: ExternalClinicalHistoryFilter): void {
		this.filters = filters;
		this.applyFilters();

	}

	public hasInformation$(): Observable<boolean> {
		return this.hasInfo$;
	}

	private applyFilters(): void {
		this.filteredHistories = this.externalClinicalHistoryList.filter(
			(history: ExternalClinicalHistoryDto) => {
				let meets: boolean;

				meets = ((this.filters.keyWord && this.keyWordFound(this.filters.keyWord, history.notes)) || !this.filters.keyWord);
				meets = meets && ((this.filters.specialty && history.professionalSpecialty == this.filters.specialty) || !this.filters.specialty);
				meets = meets && ((this.filters.professional && history.professionalName == this.filters.professional) || !this.filters.professional);
				meets = meets && ((this.filters.institution && history.institution == this.filters.institution) || !this.filters.institution);

				const historyDate: Moment = dateToMoment(dateDtoToDate(history.consultationDate));
				meets = meets && ((this.filters.consultationDate && historyDate.isSame(this.filters.consultationDate)) || !this.filters.consultationDate);

				return meets;
			}
		);
		this.filteredHistoriesSubject.next(this.filteredHistories);
	}

	private loadFiltersOptions(): ExternalClinicalHistoryFiltersOptions {
		const options: ExternalClinicalHistoryFiltersOptions = {
			specialties: [],
			professionals: [],
			institutions: []
		}

		this.externalClinicalHistoryList.forEach(
			(history: ExternalClinicalHistoryDto) => {
				if (history.professionalSpecialty && !options.specialties.find(specialty => specialty == history.professionalSpecialty))
					options.specialties.push(history.professionalSpecialty);

				if (history.professionalName && !options.professionals.find(professional => professional == history.professionalName))
					options.professionals.push(history.professionalName);

				if (history.institution && !options.institutions.find(institution => institution == history.institution))
					options.institutions.push(history.institution);
			}
		);

		return options;
	}

	private keyWordFound(word: string, notes: string): boolean {
		const regExp: RegExp = new RegExp(word.trim(), 'i');
		return notes.match(regExp) ? true : false;
	}

}
