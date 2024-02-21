import { SharedSnomedDto, ClinicalSpecialtyDto, CareLineDto, InstitutionBasicInfoDto, InstitutionalGroupDto } from "@api-rest/api-model";
import { filter } from "@presentation/components/filters-select/filters-select.component";
import { FilterTypeahead, FiltersType } from "@presentation/components/filters/filters.component";
import { careLinesToTypeaheadOptions, destinationDepartamentsToTypeaheadOptions, destinationInstitutionsToTypeaheadOptions, institutionalGroupsToTypeaheadOptions, practicesToTypeaheadOptions, specialtiesToTypeaheadOptions } from "@access-management/utils/mapper.utils";
import { ATTENTION_STATE, CLOSURE_OPTIONS, PRIORITY_OPTIONS, REGULATION_OPTIONS } from "@access-management/constants/reference";
import { AddressProjection } from "@api-rest/services/address-master-data.service";

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
    DESTINATION_INSTITUTIONS = 'destinationInstitutions',
	DESTINATION_DEARTAMENTS = 'destinationDepartaments',
	DESTINATION_NETWORKING_INSTITUTIONS = 'destinationNetworkingInstitutions',
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


export const getReportFiltersForManagers = (practices: SharedSnomedDto[], clinicalSpecialties: ClinicalSpecialtyDto[], careLines: CareLineDto[], destinationInstitutions: InstitutionBasicInfoDto[],
	institutionalGroups: InstitutionalGroupDto[], destinationDepartaments: AddressProjection[]): FiltersType => {

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
			name: "access-management.search_references.DESTINATION_INSTITUTION",
			typeaheadOption: {
				key: EDashboardFilters.DESTINATION_INSTITUTIONS,
				options: destinationInstitutionsToTypeaheadOptions(destinationInstitutions)
			}
		},
		{
			name: "access-management.search_references.INSTITUTION_NETWORK",
			typeaheadOption: {
				key: EDashboardFilters.DESTINATION_NETWORKING_INSTITUTIONS,
				options: institutionalGroupsToTypeaheadOptions(institutionalGroups)
			}
		},
		{
			name: "access-management.search_references.DEPARTMENT_DESTINATION",
			typeaheadOption: {
				key: EDashboardFilters.DESTINATION_DEARTAMENTS,
				options: destinationDepartamentsToTypeaheadOptions(destinationDepartaments)
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
			options: REGULATION_OPTIONS,
		}
	];

