import { EmergencyCarePatientDto } from "@api-rest/api-model";
import { PatientSummary } from "@hsi-components/patient-summary/patient-summary.component";

export const toPatientSummary = (patient: EmergencyCarePatientDto): PatientSummary => {
    const { firstName, lastName } = patient.person;
    return {
        fullName: `${firstName} ${lastName}`,
        identification: {
            type: patient.person.identificationType,
            number: patient.person.identificationNumber
        },
        id: patient.id,
    }
}