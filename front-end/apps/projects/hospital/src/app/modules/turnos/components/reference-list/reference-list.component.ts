import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { Report } from '../report-information/report-information.component';
import { APPOINTMENT_STATE, CLOSURE_OPTIONS, PROORITY_OPTIONS, getColoredIconText, getPriority, getReferenceState } from '@turnos/utils/reference.utils';
import { filter } from '@presentation/components/filters-select/filters-select.component';
import { PAGE_MIN_SIZE } from '@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications';

@Component({
	selector: 'app-reference-list',
	templateUrl: './reference-list.component.html',
	styleUrls: ['./reference-list.component.scss']
})
export class ReferenceListComponent {

	pageLength = PAGE_MIN_SIZE;
	filteredReferenceReports: Report[] = [];
	referenceListWithoutFilterByName: Report[] = [];
	referenceReports: Report[] = [];
	filters: filter[] = [];
	clinicalSpecialtyOptions = [];
	priorityOptions = PROORITY_OPTIONS;
	closureOptions = CLOSURE_OPTIONS;
	appoitmentStatate = APPOINTMENT_STATE;
	allReferenceReports: Report[] = [];

	@Input()
	set reports(list: ReferenceReportDto[]) {
		if (list?.length) {
			this.allReferenceReports = list.map(report => {
				const state = getReferenceState(report.appointmentStateId);
				return {
					dto: report,
					priority: getPriority(report.priority.id),
					coloredIconText: getColoredIconText(report.closureType),
					state,
				}
			});
			this.prepareFilterClinicalSpecialty(list);
			this.prepareFilters()
		}
		else
			this.allReferenceReports = [];
		this.referenceReports = this.allReferenceReports;
		this.referenceListWithoutFilterByName = this.allReferenceReports;

	};

	constructor(
		private readonly changeDetectorRef: ChangeDetectorRef,
	) { }

	changeView(result: Report[]) {
		this.pageLength = result.length;
		this.filteredReferenceReports = result;
		this.changeDetectorRef.detectChanges();
	}

	applyFilters($event: any) {

		let referenceReportsFilters = this.allReferenceReports;
		if ($event?.priority)
			referenceReportsFilters = referenceReportsFilters.filter((r: Report) => r.dto.priority.id === $event.priority);

		if ($event?.closureType) {
			referenceReportsFilters = referenceReportsFilters.filter((r: Report) => {
				if (!r.dto?.closureType)
					if ($event.closureType === -1)
						return true
				return (r.dto?.closureType?.id === $event.closureType)
			});
		}

		if ($event?.clinicalSpecialty) {
			let clinicalSpecialty = this.clinicalSpecialtyOptions.find(r => r?.id === $event.clinicalSpecialty).description;
			referenceReportsFilters = referenceReportsFilters.filter((r: Report) => r.dto.clinicalSpecialtyDestination === clinicalSpecialty);
		}

		if ($event?.appoitmentState) {
			referenceReportsFilters = referenceReportsFilters.filter((r: Report) => {
				if (!r.dto?.appointmentStateId)
					return ($event.appoitmentState === -1)
				return (r.dto.appointmentStateId === $event.appoitmentState)
			});
		}

		this.referenceReports = referenceReportsFilters;
		this.referenceListWithoutFilterByName = referenceReportsFilters;
		this.filteredReferenceReports = referenceReportsFilters.slice(0, this.pageLength);
		this.changeDetectorRef.detectChanges();
	}

	private prepareFilterClinicalSpecialty(reports: any) {
		this.clinicalSpecialtyOptions = reports.map(r => r.institutionDestination);

		this.clinicalSpecialtyOptions = reports.map(r =>
			r?.clinicalSpecialtyDestination);

		this.clinicalSpecialtyOptions = this.clinicalSpecialtyOptions.filter((especialidad) => especialidad !== undefined && especialidad !== null);

		this.clinicalSpecialtyOptions = Array.from(new Set(this.clinicalSpecialtyOptions));

		this.clinicalSpecialtyOptions = this.clinicalSpecialtyOptions.map((specialty, index) => {
			return {
				id: index + 1,
				description: specialty
			};
		});
	}

	private prepareFilters() {
		let filters = [];
		let filterCareLines: filter = {
			key: 'priority',
			name: "turnos.search_references.PRIORITY",
			options: this.priorityOptions,
		}
		filters.push(filterCareLines);

		let filterClosureType: filter = {
			key: 'closureType',
			name: "turnos.search_references.REQUEST_STATUS",
			options: this.closureOptions,
		}
		filters.push(filterClosureType);

		let filterClinicalSpecialtyDestination: filter = {
			key: 'clinicalSpecialty',
			name: "turnos.search_references.SPECIALTY",
			options: this.clinicalSpecialtyOptions,
		}
		filters.push(filterClinicalSpecialtyDestination);

		let filterAppoitmentStatate: filter = {
			key: 'appoitmentState',
			name: "turnos.search_references.SHIFT_STATUS",
			options: this.appoitmentStatate,
		}
		filters.push(filterAppoitmentStatate);
		this.filters = filters;
	}


}
