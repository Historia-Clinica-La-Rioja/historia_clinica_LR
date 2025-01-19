import { FormGroup } from "@angular/forms";
import { ThirdPartyAppointmentSearchForm } from "@call-center/components/search-appointments-for-third-party/search-appointments-for-third-party.component";
import { ThirdPartyAppointmentFilter } from "@turnos/services/diary-available-appointments-search.service";
import { toApiFormat } from '@api-rest/mapper/date.mapper';

export function getFiltersToSearch (thirdPartyAppointmentForm: FormGroup<ThirdPartyAppointmentSearchForm>): ThirdPartyAppointmentFilter {
    const initialSearchDate = toApiFormat(thirdPartyAppointmentForm.value.startDate);
    const endSearchDate = toApiFormat(thirdPartyAppointmentForm.value.endDate);

    let filters: ThirdPartyAppointmentFilter = {
        initialSearchDate,
        endSearchDate,
        departmentId: thirdPartyAppointmentForm.value.departmentId
    }

    const additionalFilters = {
        institutionId: thirdPartyAppointmentForm.value.institutionId,
        clinicalSpecialtyIds: thirdPartyAppointmentForm.value.specialtyId ? [thirdPartyAppointmentForm.value.specialtyId] : null,
        healthcareProfessionalId: thirdPartyAppointmentForm.value.professionalId,
        practiceId: thirdPartyAppointmentForm.value.practiceId,
    };

    Object.entries(additionalFilters).forEach(([key, value]) => {
        if (value)
            filters = { ...filters, [key]: value };
    });

    return filters;
}