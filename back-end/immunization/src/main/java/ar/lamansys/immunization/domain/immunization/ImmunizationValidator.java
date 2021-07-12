package ar.lamansys.immunization.domain.immunization;

import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;

public class ImmunizationValidator {

    private final VaccineSchemeStorage vaccineSchemeStorage;

    public ImmunizationValidator(VaccineSchemeStorage vaccineSchemeStorage) {
        this.vaccineSchemeStorage = vaccineSchemeStorage;
    }

    public void isValid(ImmunizationInfoBo immunizationInfoBo){
        if (immunizationInfoBo.getVaccine() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.NULL_VACCINE,
                    "La información de la vacuna es obligatoria");
        if (immunizationInfoBo.isBillable() && immunizationInfoBo.getInstitutionId() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.NULL_INSTITUTION_ID,
                    "La institución es obligatoria para una vacuna facturable");
        if (immunizationInfoBo.isBillable() && immunizationInfoBo.getAdministrationDate() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.NULL_ADMINISTRATION_DATE,
                    "La fecha de administración es obligatoria para una vacuna facturable");
        if (immunizationInfoBo.isBillable() && immunizationInfoBo.getCondition() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.NULL_CONDITION_ID,
                    "La condición de aplicación es obligatoria para una vacuna facturable");
        if (immunizationInfoBo.isBillable() && immunizationInfoBo.getSchemeId() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.NULL_SCHEME_ID,
                    "El esquema es obligatorio para una vacuna facturable");
        if (!vaccineSchemeStorage.isValidScheme(immunizationInfoBo.getSchemeId()))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_SCHEME_ID,
                    String.format("La vacuna %s tiene un esquema invalido %s", immunizationInfoBo.getVaccineName(),
                            immunizationInfoBo.getSchemeId()));
        if (immunizationInfoBo.isBillable() && immunizationInfoBo.getDose() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.NULL_DOSE_ID,
                    "La dosis es obligatoria para una vacuna facturable");

    }
}
