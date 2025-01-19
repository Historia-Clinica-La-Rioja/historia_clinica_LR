import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { ReferenceReportDto } from '@api-rest/api-model';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { FiltersType, SelectedFilterOption, SelectedFilters } from '@presentation/components/filters/filters.component'
import { differenceInDays } from 'date-fns';
import { DashboardService } from '@access-management/services/dashboard.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { PracticesService } from '@api-rest/services/practices.service';
import { lastValueFrom, take } from 'rxjs';
import { DashboardFiltersMapping, getReportFiltersForManagers, getReportFiltersForOthersRoles} from '@access-management/constants/reference-dashboard-filters';
import { dateMinusDays } from '@core/utils/date.utils';
import { CareLineService } from '@api-rest/services/care-line.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { InstitutionalGroupsService } from '@api-rest/services/institutional-groups.service';
import { MANAGER_ROLES } from '../../../home/constants/menu';
import { PermissionsService } from '@core/services/permissions.service';

const MAX_DAYS = 90;

@Component({
	selector: 'app-reference-dashboard-filters',
	templateUrl: './reference-dashboard-filters.component.html',
	styleUrls: ['./reference-dashboard-filters.component.scss']
})
export class ReferenceDashboardFiltersComponent implements OnInit {
	dashboardView = DashboardView;
	showValidation = false;
	reports: ReferenceReportDto[] = [];
	filterByDocument: FormGroup<SearchByDocument>;
	filters: FiltersType;
	today = new Date();

	constructor(
		readonly dashboardService: DashboardService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly practiceService: PracticesService,
		private readonly changeDetectorRef: ChangeDetectorRef,
		private readonly careLineService: CareLineService,
		private readonly institutionService: InstitutionService,
		private readonly institutionalGroupsService: InstitutionalGroupsService,
		private readonly permissionService: PermissionsService,

	) { }

	ngOnInit(): void {

		this.filterByDocument = new FormGroup<SearchByDocument>({
			description: new FormControl(null),
		});

		this.dashboardService.dateRange = {
			start: dateMinusDays(this.today, MAX_DAYS),
			end: this.today
		}

		this.setFiltersOptions();

		this.dashboardService.updateReports();
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
		this.searchByDocument();
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

		this.permissionService.hasContextAssignments$(MANAGER_ROLES)
			.pipe(take(1))
			.subscribe((hasRoleOfManager: boolean) =>
				this.filtersOptions(hasRoleOfManager)
			);
	}

	private async filtersOptions(hasRoleOfManager: boolean) {
		const [practices, clinicalSpecialties, careLines] = await Promise.all([
			lastValueFrom(this.practiceService.getAll()),
			lastValueFrom(this.clinicalSpecialtyService.getAll()),
			lastValueFrom(this.careLineService.getAllCareLines())
		]);

		if (hasRoleOfManager) {
			const [originInstitution, institutionalGroups] = await Promise.all([
				lastValueFrom(this.institutionService.getInstitutionsByManagerUser()),
				lastValueFrom(this.institutionalGroupsService.getCurrentUserGroups())
			]);

			this.filters = getReportFiltersForManagers(practices, clinicalSpecialties, careLines, originInstitution, institutionalGroups);
		} else {
			this.filters = getReportFiltersForOthersRoles(practices, clinicalSpecialties, careLines);
		}

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
