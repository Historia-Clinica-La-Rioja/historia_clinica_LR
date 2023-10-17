import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { Report } from '../report-information/report-information.component';
import { APPOINTMENT_STATE, CLOSURE_OPTIONS, PRIORITY_OPTIONS, getColoredIconText, getPriority, getState } from '@turnos/utils/reference.utils';
import { filter } from '@presentation/components/filters-select/filters-select.component';
import { PAGE_MIN_SIZE } from '@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications';
import { AbstractControl, FormBuilder, UntypedFormGroup } from '@angular/forms';
import { REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import { ReportCompleteDataPopupComponent } from '@turnos/dialogs/report-complete-data-popup/report-complete-data-popup.component';
import { MatDialog } from '@angular/material/dialog';

const REFERENCE_REQUESTED = -1;
const PENDING_CLOSURE = -1;

@Component({
	selector: 'app-reference-list',
	templateUrl: './reference-list.component.html',
	styleUrls: ['./reference-list.component.scss']
})
export class ReferenceListComponent {
	filteredReferenceReports: Report[] = [];
	filteredReferenceByName: Report[] = [];
	referenceReports: Report[] = [];
	filters: filter[] = [];
	allReferenceReports: Report[] = [];
	formFilter: UntypedFormGroup;
	filteredByName = false;
	private applySearchFilter = '';
	private priorityOptions = PRIORITY_OPTIONS;
	private closureOptions = CLOSURE_OPTIONS;
	private appoitmentStatate = APPOINTMENT_STATE;
	private pageLength = PAGE_MIN_SIZE;
	private clinicalSpecialtyOptions = [];
	private procedureOptions = [];
	private referenceListWithoutFilterByName: Report[] = [];
	private filtersCustom;
	@Input()
	set reports(list: ReferenceReportDto[]) {
		if (list?.length) {
			this.allReferenceReports = list.map(report => {
				const state = getState(report.appointmentStateId);
				return {
					dto: report,
					priority: getPriority(report.priority.id),
					coloredIconText: getColoredIconText(report.closureType),
					state,
				}
			});
			this.prepareFilterClinicalSpecialty(list);
			this.prepareFilterProcedure(list);
			this.prepareFilters()
		}
		else
			this.allReferenceReports = [];
		this.formFilter?.reset();
		this.filteredByName = false;
		this.referenceReports = this.allReferenceReports;
		this.referenceListWithoutFilterByName = this.allReferenceReports;
	};

	constructor(
		private readonly changeDetectorRef: ChangeDetectorRef,
		readonly formBuilder: FormBuilder,
		private readonly dialog: MatDialog,
	) { }

	ngOnInit() {
		this.formFilter = this.formBuilder.group({
			description: [null]
		});
	}

	changeView(result: Report[]) {
		this.pageLength = result.length;
		this.filteredReferenceReports = result;
		this.changeDetectorRef.detectChanges();
	}

	applyFiltersCustom($event: any) {
		this.filtersCustom = $event;
		this.applyFilters(this.filtersCustom);
	}

	clearFilterField(control: AbstractControl) {
		this.filteredByName = false;
		this.filteredReferenceByName = [];
		control.reset();
		this.filteredReferenceReports = this.referenceListWithoutFilterByName.slice(0, this.pageLength);
		this.applyFilters(this.filtersCustom);
	}

	applyFilterByNameAndDocument($event: KeyboardEvent) {
		this.applySearchFilter = ($event?.target as HTMLInputElement).value?.replace(REMOVE_SUBSTRING_DNI, '') || '';
		const filterLowerCase = this.applySearchFilter.toLowerCase();
		const list = this.referenceListWithoutFilterByName.filter((r: Report) => {
		  const patientFullName = r.dto.patientFullName.toLowerCase();
		  const identificationNumber = r.dto?.identificationNumber?.toString() || '';

		  return patientFullName.includes(filterLowerCase) || identificationNumber.includes(filterLowerCase);
		});

		this.filteredByName = filterLowerCase.length > 0;
		this.filteredReferenceByName = this.filteredByName ? list : [];
		this.applyFilters(this.filtersCustom);
	}

	private applyFilters($event: any) {
		let referenceReportsFilters = this.filteredByName ? this.filteredReferenceByName : this.allReferenceReports;

		referenceReportsFilters = this.filterByPriority(referenceReportsFilters, $event?.priority);
		referenceReportsFilters = this.filterByClosureType(referenceReportsFilters, $event?.closureType);
		referenceReportsFilters = this.filterByAppointmentState(referenceReportsFilters, $event?.appoitmentState);

		const clinicalSpecialty = this.getClinicalSpecialtyDescription($event.clinicalSpecialty);
		if (clinicalSpecialty) {
			referenceReportsFilters = this.filterByClinicalSpecialty(referenceReportsFilters, clinicalSpecialty);
		}
		const procedure = this.getProcedureDescription($event.procedure)
		if (procedure) {
			referenceReportsFilters = this.filterByProcedure(referenceReportsFilters, procedure);
		}

		this.updateReferenceReports(referenceReportsFilters);
	}

	private filterByPriority(reports: Report[], priority: any): Report[] {
		return priority ? reports.filter((r: Report) => r.dto.priority.id === priority) : reports;
	}

	private filterByClosureType(reports: Report[], closureType: any): Report[] {
		return this.filterByNullableField(reports, closureType, (r: Report) => r.dto?.closureType?.id, REFERENCE_REQUESTED);
	}

	private filterByAppointmentState(reports: Report[], appointmentState: any): Report[] {
		return this.filterByNullableField(reports, appointmentState, (r: Report) => r.dto?.appointmentStateId, PENDING_CLOSURE);
	}

	private filterByNullableField(reports: Report[], value: any, fieldSelector: (r: Report) => any, valueFilter: number): Report[] {
		if (value) {
			return reports.filter((r: Report) => {
				const fieldValue = fieldSelector(r);
				return !fieldValue && value === valueFilter || fieldValue === value;
			});
		}
		return reports;
	}

	private getClinicalSpecialtyDescription(clinicalSpecialtyId: any): string | undefined {
		return this.clinicalSpecialtyOptions.find(r => r?.id === clinicalSpecialtyId)?.description;
	}

	private filterByClinicalSpecialty(reports: Report[], clinicalSpecialty: string): Report[] {
		return reports.filter((r: Report) => r.dto.clinicalSpecialtyDestination === clinicalSpecialty);
	}

	private getProcedureDescription(procedureId: number): string | undefined {
		return this.procedureOptions.find(p => p?.id === procedureId)?.description;
	}

	private filterByProcedure(reports: Report[], procedure: string): Report[] {
		return reports.filter((r: Report) => r.dto.procedure === procedure);
	}

	private updateReferenceReports(filteredReports: Report[]) {
		this.referenceReports = filteredReports;
		if (!this.filteredByName)
			this.referenceListWithoutFilterByName = filteredReports;
		this.filteredReferenceReports = filteredReports.slice(0, this.pageLength);
		this.changeDetectorRef.detectChanges();
	}

	private prepareFilterClinicalSpecialty(reports: ReferenceReportDto[]) {
		this.clinicalSpecialtyOptions = reports.map(r => r.institutionDestination);

		this.clinicalSpecialtyOptions = reports.map(r => r?.clinicalSpecialtyDestination);

		this.clinicalSpecialtyOptions = this.clinicalSpecialtyOptions.filter((especialidad) => especialidad !== undefined && especialidad !== null);

		this.clinicalSpecialtyOptions = Array.from(new Set(this.clinicalSpecialtyOptions));

		this.clinicalSpecialtyOptions = this.clinicalSpecialtyOptions.map((specialty, index) => {
			return {
				id: index + 1,
				description: specialty
			};
		});
	}

	private prepareFilterProcedure(reports: ReferenceReportDto[]) {
		this.procedureOptions = reports.map(r => r.procedure );

		this.procedureOptions = this.procedureOptions.filter((procedure) => procedure !== undefined && procedure !== null);

		this.procedureOptions = Array.from(new Set(this.procedureOptions));

		this.procedureOptions = this.procedureOptions.map((procedure, index) => {
			return {
				id: index + 1,
				description: procedure
			};
		});
	}

	private prepareFilters() {
		let filters = [];
		let filterClosureType: filter = {
			key: 'closureType',
			name: "turnos.search_references.REQUEST_STATUS",
			options: this.closureOptions,
		}
		filters.push(filterClosureType);

		let filterProcedure: filter = {
			key: 'procedure',
			name: "turnos.PRACTICE",
			options: this.procedureOptions,
		}
		filters.push(filterProcedure);

		let filterAppoitmentStatate: filter = {
			key: 'appoitmentState',
			name: "turnos.search_references.SHIFT_STATUS",
			options: this.appoitmentStatate,
		}
		filters.push(filterAppoitmentStatate);

		let filterClinicalSpecialtyDestination: filter = {
			key: 'clinicalSpecialty',
			name: "turnos.search_references.SPECIALTY",
			options: this.clinicalSpecialtyOptions,
		}
		filters.push(filterClinicalSpecialtyDestination);

		let filterCareLines: filter = {
			key: 'priority',
			name: "turnos.search_references.PRIORITY",
			options: this.priorityOptions,
		}
		filters.push(filterCareLines);

		this.filters = filters;
	}

	openRequest(referenceId: number) {
		this.dialog.open(ReportCompleteDataPopupComponent, {
			data: {
				referenceId
			},
			autoFocus: false,
			disableClose: true,
			width: '40%',
		});
	}

}
