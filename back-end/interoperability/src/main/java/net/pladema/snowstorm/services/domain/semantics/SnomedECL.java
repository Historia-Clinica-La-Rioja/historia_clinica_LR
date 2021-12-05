package net.pladema.snowstorm.services.domain.semantics;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

public enum SnomedECL {
    BLOOD_TYPE, PERSONAL_RECORD, FAMILY_RECORD, ALLERGY, HOSPITAL_REASON,
    VACCINE, MEDICINE, PROCEDURE, CONSULTATION_REASON, DIAGNOSIS;

    public static SnomedECL map(String id) {
        try {
            return valueOf(id);
        } catch (Exception e) {
            throw new NotFoundException("snomed-ecl-not-exists", String.format("La ecl %s no existe", id));
        }
    }

}
