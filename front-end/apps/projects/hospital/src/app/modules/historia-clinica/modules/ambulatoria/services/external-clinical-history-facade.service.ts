import { Injectable } from '@angular/core';
import { ExternalClinicalHistoryDto } from '@api-rest/api-model';
import { ExternalClinicalHistoryService } from '@api-rest/services/external-clinical-history.service';
import { Observable, ReplaySubject } from 'rxjs';
import { ExternalClinicalHistoryFiltersOptions } from '../components/external-clinical-histories-filters/external-clinical-histories-filters.component';

@Injectable({
	providedIn: 'root'
})
export class ExternalClinicalHistoryFacadeService {

	private externalClinicalHistoryList: ExternalClinicalHistoryDto[] = [];
	private externalHistoriesSubject: ReplaySubject<ExternalClinicalHistoryDto[]> = new ReplaySubject<ExternalClinicalHistoryDto[]>(1);
	private externalClinicalHistoryList$: Observable<ExternalClinicalHistoryDto[]>;

	private filtersOptions: ExternalClinicalHistoryFiltersOptions;
	private filtersOptionsSubject: ReplaySubject<ExternalClinicalHistoryFiltersOptions> = new ReplaySubject<ExternalClinicalHistoryFiltersOptions>(1);
	private filtersOptions$: Observable<ExternalClinicalHistoryFiltersOptions>;

	constructor(private readonly externalClinicalHistoryService: ExternalClinicalHistoryService) {
		this.externalClinicalHistoryList$ = this.externalHistoriesSubject.asObservable();
		this.filtersOptions$ = this.filtersOptionsSubject.asObservable();
	}

	// It's necessary to invoke this method to bring the information from Backend
	public setPatientId(patientId: number): void {
		// With this IF statement, I make sure to subscribe only once to fetch data from Backend
		if (!this.externalClinicalHistoryList.length)
			this.externalClinicalHistoryService.getExternalClinicalHistoryList(patientId).subscribe(
				(externalClinicalHistories: ExternalClinicalHistoryDto[]) => {
					this.externalClinicalHistoryList = externalClinicalHistories;
					this.externalHistoriesSubject.next(this.externalClinicalHistoryList);
					this.filtersOptions = this.loadFiltersOptions();
					this.filtersOptionsSubject.next(this.filtersOptions);
				}
			);
	}

	public getFilteredHistories(): Observable<ExternalClinicalHistoryDto[]> {
		return this.externalClinicalHistoryList$; // ahora tiene todas, pero deberia devolver filtrado
	}

	public getFiltersOptions(): Observable<ExternalClinicalHistoryFiltersOptions> {
		return this.filtersOptions$;
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

}
