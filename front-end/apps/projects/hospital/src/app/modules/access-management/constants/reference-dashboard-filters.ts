import { SharedSnomedDto, ClinicalSpecialtyDto, ERole } from "@api-rest/api-model";
import { filter } from "@presentation/components/filters-select/filters-select.component";
import { FilterTypeahead, FiltersType } from "@presentation/components/filters/filters.component";
import { practicesToTypeaheadOptions, specialtiesToTypeaheadOptions } from "@turnos/utils/mapper.utils";
import { CLOSURE_OPTIONS, APPOINTMENT_STATE, PRIORITY_OPTIONS } from "@turnos/utils/reference.utils";

export enum EDashboardFilters {
	CLOSURE_TYPE = 'closureType',
	APPOINTMENT_STATE = 'appointmentState',
	PRIORITY = 'priority',
	PRACTICE = 'practice',
	SPECIALTY = 'specialty',
	IDENTIFICATION_NUMBER = 'identificationNumber'
}

export const DashboardFiltersMapping = {
	[EDashboardFilters.CLOSURE_TYPE]: 'closureTypeId',
	[EDashboardFilters.APPOINTMENT_STATE]: 'appointmentStateId',
	[EDashboardFilters.PRIORITY]: 'priorityId',
	[EDashboardFilters.PRACTICE]: 'procedureId',
	[EDashboardFilters.SPECIALTY]: 'clinicalSpecialtyId',
	[EDashboardFilters.IDENTIFICATION_NUMBER]: 'identificationNumber'
}

export const setReportFilters = (practices: SharedSnomedDto[], clinicalSpecialties: ClinicalSpecialtyDto[]): FiltersType => {
	const filterSelects: filter[] = [
		{
			key: EDashboardFilters.CLOSURE_TYPE,
			name: "access-management.search_references.REQUEST_STATUS",
			options: CLOSURE_OPTIONS,
		},
		{
			key: EDashboardFilters.APPOINTMENT_STATE,
			name: "access-management.search_references.SHIFT_STATUS",
			options: APPOINTMENT_STATE,
		},
		{
			key: EDashboardFilters.PRIORITY,
			name: "access-management.search_references.PRIORITY",
			options: PRIORITY_OPTIONS,
		},
	];

	const filterTypeahead: FilterTypeahead[] = [
		{
			name: "access-management.search_references.PRACTICE",
			typeaheadOption: {
				key: EDashboardFilters.PRACTICE,
				options: practicesToTypeaheadOptions(practices)
			}
		},
		{
			name: "access-management.search_references.SPECIALTY",
			typeaheadOption: {
				key: EDashboardFilters.SPECIALTY,
				options: specialtiesToTypeaheadOptions(clinicalSpecialties)
			}
		},
	];

	return {
		selects: filterSelects,
		typeaheads: filterTypeahead,
	};
};

export const DashboardRolesMapping = {
	[ERole.ADMINISTRATIVO]: 'closureTypeId',
	[EDashboardFilters.APPOINTMENT_STATE]: 'appointmentStateId',

}