import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { ReferenceReportDto } from '@api-rest/api-model';
import { dateMinusDays } from '@core/utils/date.utils';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { FiltersType, SelectedFilterOption, SelectedFilters } from '@presentation/components/filters/filters.component'
import { differenceInDays } from 'date-fns';
import { DashboardService } from '@access-management/services/dashboard.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { PracticesService } from '@api-rest/services/practices.service';
import { forkJoin } from 'rxjs';
import { DashboardFiltersMapping, setReportFilters } from '@access-management/constants/reference-dashboard-filters';

const MAX_DAYS = 90;

@Component({
	selector: 'app-reference-dashboard-filters',
	templateUrl: './reference-dashboard-filters.component.html',
	styleUrls: ['./reference-dashboard-filters.component.scss']
})
export class ReferenceDashboardFiltersComponent implements OnInit, OnDestroy {
	dashboardView = DashboardView;
	showValidation = false;
	reports: ReferenceReportDto[] = [];
	filterByDocument: FormGroup<SearchByDocument>;
	filters: FiltersType;

	constructor(
		readonly dashboardService: DashboardService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly practiceService: PracticesService,
		private readonly changeDetectorRef: ChangeDetectorRef,
	) { }

	ngOnInit(): void {

		this.filterByDocument = new FormGroup<SearchByDocument>({
			description: new FormControl(null),
		});

		const today = new Date();
		this.dashboardService.dateRange = {
			start: dateMinusDays(today, MAX_DAYS),
			end: today
		}

		this.setFiltersOptions();

		this.dashboardService.updateReports();
	}

	ngOnDestroy(): void {
		this.dashboardService.initializeFilters();
	}

	checkDays(dateRange: DateRange) {
		this.showValidation = false;
		if (differenceInDays(dateRange.end, dateRange.start) > MAX_DAYS) {
			this.showValidation = true;
			this.reports = [];
			return;
		}
		this.dashboardService.dateRange = dateRange;
		this.updatePaginatorAndReports();
		this.changeDetectorRef.detectChanges();

	}

	clearFilterField(control: AbstractControl) {
		control.reset();
	}

	setFilters(selectedFilters: SelectedFilters) {
		let appliedFilters: DashboardFilters = {} as DashboardFilters;

		const results = this.concatFilters(selectedFilters);

		results?.forEach(filterOptions => {
			const key = filterOptions.key;
			const value = filterOptions.value;
			appliedFilters[DashboardFiltersMapping[key]] = value;
		});
		appliedFilters = this.setDocumentFilter(appliedFilters);
		this.dashboardService.dashboardFilters = appliedFilters;
		this.updatePaginatorAndReports();
	}

	searchByDocument() {
		this.dashboardService.dashboardFilters = this.setDocumentFilter(this.dashboardService.dashboardFilters);
		this.updatePaginatorAndReports();
	}

	private setDocumentFilter(dashboardFilters: DashboardFilters): DashboardFilters {
		const document = this.filterByDocument.value.description;
		dashboardFilters = {
			...dashboardFilters,
			identificationNumber: document ? document : null
		}
		return dashboardFilters;
	}

	private updatePaginatorAndReports() {
		this.dashboardService.pageNumber = 0;
		this.dashboardService.updateReports();
	}

	private concatFilters(filters: SelectedFilters): SelectedFilterOption[] {
		if (!filters) return null;

		const selectFilters = filters.select || [];
		const typeaheadFilters = filters.typeahead || [];

		return [...selectFilters, ...typeaheadFilters];
	}

	private setFiltersOptions() {

		const clinicalSpecialties$ = this.clinicalSpecialtyService.getAll();
		const practices$ = this.practiceService.getAll();

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
	regulationStateId?: number;
	from: string;
	to: string;
}
