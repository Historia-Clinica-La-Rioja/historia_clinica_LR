import { SharedSnomedDto, ClinicalSpecialtyDto } from "@api-rest/api-model";
import { filter } from "@presentation/components/filters-select/filters-select.component";
import { FilterTypeahead, FiltersType } from "@presentation/components/filters/filters.component";
import { practicesToTypeaheadOptions, specialtiesToTypeaheadOptions } from "@access-management/utils/mapper.utils";
import { ATTENTION_STATE, CLOSURE_OPTIONS, PRIORITY_OPTIONS, REGULATION_OPTIONS } from "@access-management/constants/reference";

export enum EDashboardFilters {
    CLOSURE_TYPE = 'closureType',
    APPOINTMENT_STATE = 'appointmentState',
    ATTENTION_STATE = 'attentionState',
    PRIORITY = 'priority',
    PRACTICE = 'practice',
    SPECIALTY = 'specialty',
    IDENTIFICATION_NUMBER = 'identificationNumber',
    REGULATION_STATES = 'regulationState'
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

export const setReportFilters = (practices: SharedSnomedDto[], clinicalSpecialties: ClinicalSpecialtyDto[]): FiltersType => {
    const filterSelects: filter[] = [
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
