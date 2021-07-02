package ar.lamansys.immunization.domain.immunization;

import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;

public class ImmunizationValidator {

    private final VaccineSchemeStorage vaccineSchemeStorage;

    public ImmunizationValidator(VaccineSchemeStorage vaccineSchemeStorage) {
        this.vaccineSchemeStorage = vaccineSchemeStorage;
    }

    public boolean isValid(ImmunizationInfoBo immunizationInfoBo){
        if (!vaccineSchemeStorage.isValidScheme(immunizationInfoBo.getSchemeId()))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_SCHEME_ID,
                    String.format("La vacuna %s tiene un esquema invalido %s", immunizationInfoBo.getVaccineName(),
                            immunizationInfoBo.getSchemeId()));

        return true;
    }
}
