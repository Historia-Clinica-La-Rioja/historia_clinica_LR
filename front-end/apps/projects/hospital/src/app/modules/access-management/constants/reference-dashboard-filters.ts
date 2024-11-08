import { SharedSnomedDto, ClinicalSpecialtyDto, CareLineDto, InstitutionBasicInfoDto, InstitutionalGroupDto } from "@api-rest/api-model";
import { filter } from "@presentation/components/filters-select/filters-select.component";
import { FilterTypeahead, FiltersType } from "@presentation/components/filters/filters.component";
import { careLinesToTypeaheadOptions, institutionalGroupsToTypeaheadOptions, institutionsToTypeaheadOptions, practicesToTypeaheadOptions, specialtiesToTypeaheadOptions } from "@access-management/utils/mapper.utils";
import { ATTENTION_STATE, CLOSURE_OPTIONS, PRIORITY_OPTIONS, APPROVAL_OPTIONS } from "@access-management/constants/reference";


export enum EDashboardFilters {
    CLOSURE_TYPE = 'closureType',
    APPOINTMENT_STATE = 'appointmentState',
    ATTENTION_STATE = 'attentionState',
    PRIORITY = 'priority',
    PRACTICE = 'practice',
    SPECIALTY = 'specialty',
    IDENTIFICATION_NUMBER = 'identificationNumber',
    REGULATION_STATES = 'regulationState',
    CARE_LINE = 'careLine',
    ORIGIN_INSTITUTIONS = 'originInstitutions',
	DESTINATION_NETWORKING_INSTITUTIONS = 'institutionalGroups',
}

export const DashboardFiltersMapping = {
    [EDashboardFilters.CLOSURE_TYPE]: 'closureTypeId',
    [EDashboardFilters.APPOINTMENT_STATE]: 'appointmentStateId',
    [EDashboardFilters.ATTENTION_STATE]: 'attentionStateId',
    [EDashboardFilters.PRIORITY]: 'priorityId',
    [EDashboardFilters.PRACTICE]: 'procedureId',
    [EDashboardFilters.SPECIALTY]: 'clinicalSpecialtyId',
    [EDashboardFilters.IDENTIFICATION_NUMBER]: 'identificationNumber',
    [EDashboardFilters.REGULATION_STATES]: 'regulationStateId',
    [EDashboardFilters.ORIGIN_INSTITUTIONS]: 'originInstitutionId',
    [EDashboardFilters.CARE_LINE]: 'careLineId',
    [EDashboardFilters.DESTINATION_NETWORKING_INSTITUTIONS]: 'institutionalGroupId',
}

export const getReportFiltersForOthersRoles = (practices: SharedSnomedDto[], clinicalSpecialties: ClinicalSpecialtyDto[], careLines: CareLineDto[]): FiltersType => {
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
		{
			name: "access-management.search_references.CARE_LINE",
			typeaheadOption: {
				key: EDashboardFilters.CARE_LINE,
				options: careLinesToTypeaheadOptions(careLines)
			}
		}
	];

	return {
		selects: getFilterSelects,
		typeaheads: filterTypeahead,
	};
};


export const getReportFiltersForManagers = (practices: SharedSnomedDto[], clinicalSpecialties: ClinicalSpecialtyDto[], careLines: CareLineDto[], originInstitutions: InstitutionBasicInfoDto[],
	institutionalGroups: InstitutionalGroupDto[]): FiltersType => {

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
		{
			name: "access-management.search_references.CARE_LINE",
			typeaheadOption: {
				key: EDashboardFilters.CARE_LINE,
				options: careLinesToTypeaheadOptions(careLines)
			}
		},
		{
			name: "access-management.search_references.ORIGIN_INSTITUTION",
			typeaheadOption: {
				key: EDashboardFilters.ORIGIN_INSTITUTIONS,
				options: institutionsToTypeaheadOptions(originInstitutions)
			}
		},
		{
			name: "access-management.search_references.INSTITUTION_NETWORK",
			typeaheadOption: {
				key: EDashboardFilters.DESTINATION_NETWORKING_INSTITUTIONS,
				options: institutionalGroupsToTypeaheadOptions(institutionalGroups)
			}
		}
	];

	return {
		selects: getFilterSelects,
		typeaheads: filterTypeahead,
	};
};

const getFilterSelects: filter[] =

	[
		{
			key: EDashboardFilters.CLOSURE_TYPE,
			name: "access-management.search_references.REQUEST_STATUS",
			options: CLOSURE_OPTIONS,
		},
		{
			key: EDashboardFilters.ATTENTION_STATE,
			name: "access-management.search_references.SHIFT_STATUS",
			options: ATTENTION_STATE,
		},
		{
			key: EDashboardFilters.PRIORITY,
			name: "access-management.search_references.PRIORITY",
			options: PRIORITY_OPTIONS,
		},
		{
			key: EDashboardFilters.REGULATION_STATES,
			name: "access-management.search_references.REGULATION_STATES",
			options: APPROVAL_OPTIONS,
		}
	];

