package ar.lamansys.immunization.domain.immunization;

import ar.lamansys.immunization.domain.snomed.SnomedBo;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationBo;

public class ImmunizationValidator {

    private final VaccineSchemeStorage vaccineSchemeStorage;

    private final VaccineRuleStorage vaccineRuleStorage;

    public ImmunizationValidator(VaccineSchemeStorage vaccineSchemeStorage, VaccineRuleStorage vaccineRuleStorage) {
        this.vaccineSchemeStorage = vaccineSchemeStorage;
        this.vaccineRuleStorage = vaccineRuleStorage;
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
        if (immunizationInfoBo.getSchemeId() != null && !vaccineSchemeStorage.isValidScheme(immunizationInfoBo.getSchemeId()))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_SCHEME_ID,
                    String.format("La vacuna %s tiene un esquema invalido %s", immunizationInfoBo.getVaccineName(),
                            immunizationInfoBo.getSchemeId()));
        validRule(immunizationInfoBo.getVaccine(), immunizationInfoBo.getCondition(), immunizationInfoBo.getSchemeId(), immunizationInfoBo.getDose());
    }

    private void validRule(SnomedBo vaccine, VaccineConditionApplicationBo condition, Short schemeId, VaccineDoseBo dose) {
        if (vaccineIsNull(vaccine))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.NULL_VACCINE,
                    "La información de la vacuna es obligatoria");
        if (!doseIsNull(dose) && (condition == null || schemeId == null))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INCOMPLETE_DATA,
                    String.format("Alguna información esta incompleta condición %s, id de esquema %s, dosis %s", condition, schemeId, dose));
        if (schemeId != null && (condition == null || doseIsNull(dose)))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INCOMPLETE_DATA,
                    String.format("Alguna información esta incompleta condición %s, id de esquema %s, dosis %s", condition, schemeId, dose));
        if (condition != null && (schemeId == null || doseIsNull(dose)))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INCOMPLETE_DATA,
                    String.format("Alguna información esta incompleta condición %s, id de esquema %s, dosis %s", condition, schemeId, dose));
        if (condition == null && schemeId == null && doseIsNull(dose))
            return;
        if (!vaccineRuleStorage.existRule(vaccine.getSctid(), condition.getId(), schemeId, dose.getDescription(), dose.getOrder()))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_RULE,
                    String.format("La combinación de información no es una regla valida para nomivac -> vacuna %s," +
                            " condición %s, id de esquema %s, dosis %s", vaccine, condition, schemeId, dose));

    }

    private boolean vaccineIsNull(SnomedBo vaccine) {
        return (vaccine == null || vaccine.getSctid() == null || vaccine.getParentFsn() == null);
    }

    private boolean doseIsNull(VaccineDoseBo dose) {
        return (dose == null || dose.getDescription() == null || dose.getOrder() == null);
    }
}
