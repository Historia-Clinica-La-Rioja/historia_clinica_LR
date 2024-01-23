import { FormGroup } from "@angular/forms";
import { ThirdPartyAppointmentSearchForm } from "@call-center/components/search-appointments-for-third-party/search-appointments-for-third-party.component";
import { DateFormat } from "@core/utils/date.utils";
import { ThirdPartyAppointmentFilter } from "@turnos/services/diary-available-appointments-search.service";
import format from "date-fns/format";

export function getFiltersToSearch (thirdPartyAppointmentForm: FormGroup<ThirdPartyAppointmentSearchForm>): ThirdPartyAppointmentFilter {
    const initialSearchDate = format(thirdPartyAppointmentForm.value.startDate, DateFormat.API_DATE);
    const endSearchDate = format(thirdPartyAppointmentForm.value.endDate, DateFormat.API_DATE);

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