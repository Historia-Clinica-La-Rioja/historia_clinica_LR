import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { ReferenceReportDto } from '@api-rest/api-model';
import { dateMinusDays } from '@core/utils/date.utils';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { FiltersType, SelectedFilterOption, SelectedFilters } from '@presentation/components/filters/filters.component'
import { differenceInDays } from 'date-fns';
import { ReferenceReportFacadeService } from '../../services/reference-report-facade.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { PracticesService } from '@api-rest/services/practices.service';
import { forkJoin } from 'rxjs';
import { DashboardFiltersMapping, EDashboardFilters, setReportFilters } from '@turnos/constants/report-filters';

const MAX_DAYS = 90;

@Component({
	selector: 'app-report-filters',
	templateUrl: './report-filters.component.html',
	styleUrls: ['./report-filters.component.scss']
})
export class ReportFiltersComponent implements OnInit, OnDestroy {
	dashboardView = DashboardView;
	showValidation = false;
	reports: ReferenceReportDto[] = [];
	filterByDocument: FormGroup<SearchByDocument>;
	filters: FiltersType;

	constructor(
		readonly referenceReportFacade: ReferenceReportFacadeService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly practiceService: PracticesService,
		private readonly changeDetectorRef: ChangeDetectorRef,
	) { }

	ngOnInit(): void {

		this.filterByDocument = new FormGroup<SearchByDocument>({
			description: new FormControl(null),
		});

		const today = new Date();
		this.referenceReportFacade.dateRange = {
			start: dateMinusDays(today, MAX_DAYS),
			end: today
		}

		this.setFiltersOptions();

		this.referenceReportFacade.updateReports();
	}

	ngOnDestroy(): void {
		this.referenceReportFacade.initializeFilters();
	}

	checkDays(dateRange: DateRange) {
		this.showValidation = false;
		if (differenceInDays(dateRange.end, dateRange.start) > MAX_DAYS) {
			this.showValidation = true;
			this.reports = [];
			return;
		}
		this.referenceReportFacade.dateRange = dateRange;
		this.changeDetectorRef.detectChanges();

	}

	clearFilterField(control: AbstractControl) {
		control.reset();
	}

	setFilters(selectedFilters: SelectedFilters) {
		let appliedFilters: DashboardFilters = {} as DashboardFilters;
		const document = this.filterByDocument.value.description;

		if (document)
			appliedFilters = this.applyDocumentFilter();

		const results = this.concatFilters(selectedFilters);

		results?.forEach(filterOptions => {
			const key = filterOptions.key;
			const value = filterOptions.value;
			appliedFilters[DashboardFiltersMapping[key]] = value;
		});

		this.applyFilters(appliedFilters);
	}

	searchByDocument() {
		const document = this.filterByDocument.value.description;
		let appliedFilters: DashboardFilters = {} as DashboardFilters;
		if (document)
			appliedFilters = this.applyDocumentFilter();
		this.applyFilters(appliedFilters);
	}

	private applyFilters(appliedFilters: DashboardFilters) {
		this.referenceReportFacade.dashboardFilters = appliedFilters;
		this.referenceReportFacade.updateReports();
	}

	private applyDocumentFilter(): DashboardFilters {
		const appliedFilters: DashboardFilters = {} as DashboardFilters;
		const filterKey = EDashboardFilters.IDENTIFICATION_NUMBER;
		appliedFilters[DashboardFiltersMapping[filterKey]] = document;
		return appliedFilters;
	}

	private concatFilters(filters: SelectedFilters): SelectedFilterOption[] {
		if (!filters) return null;

		const selectFilters = filters.select || [];
		const typeaheadFilters = filters.typeahead || [];

		return [...selectFilters, ...typeaheadFilters];
	}

	private setFiltersOptions() {

		const clinicalSpecialties$ = this.clinicalSpecialtyService.getAll();
		const practices$ = this.practiceService.getPracticesFromInstitutions();

		forkJoin([clinicalSpecialties$, practices$]).subscribe(([clinicalSpecialties, practices]) =>
			this.filters = setReportFilters(practices, clinicalSpecialties));
	}

}

interface SearchByDocument {
	description: FormControl<number>;
}

export enum DashboardView {
	REQUESTED,
	RECEIVED
}

export interface DashboardFilters {
	identificationNumber?: number;
	procedureId?: number;
	appointmentStateId?: number;
	closureTypeId?: number;
	clinicalSpecialtyId?: number;
	priorityId?: number;
	from: string;
	to: string;
}
