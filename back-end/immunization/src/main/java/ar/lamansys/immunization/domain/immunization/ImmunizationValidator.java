package ar.lamansys.immunization.domain.immunization;

import ar.lamansys.immunization.domain.snomed.SnomedBo;
import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;

public class ImmunizationValidator {

    public static final String INCOMPLETE_DATA = "Para validar las reglas de cada vacuna todos los datos siguientes son obligatorios: condición(id=%s), esquema(id=%s), dosis(description=%s, order=%s)";

    private final VaccineConditionApplicationStorage vaccineConditionApplicationStorage;

    private final VaccineSchemeStorage vaccineSchemeStorage;

    private final VaccineRuleStorage vaccineRuleStorage;

    public ImmunizationValidator(VaccineConditionApplicationStorage vaccineConditionApplicationStorage,
                                 VaccineSchemeStorage vaccineSchemeStorage,
                                 VaccineRuleStorage vaccineRuleStorage) {
        this.vaccineConditionApplicationStorage = vaccineConditionApplicationStorage;
        this.vaccineSchemeStorage = vaccineSchemeStorage;
        this.vaccineRuleStorage = vaccineRuleStorage;
    }

    public void isValid(ImmunizationInfoBo immunizationInfoBo){
        validVaccine(immunizationInfoBo.getVaccine());
        if (immunizationInfoBo.isBillable() && immunizationInfoBo.getInstitutionId() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.NULL_INSTITUTION_ID,
                    "La institución es obligatoria para una vacuna facturable");
        if (immunizationInfoBo.isBillable() && immunizationInfoBo.getAdministrationDate() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.NULL_ADMINISTRATION_DATE,
                    "La fecha de administración es obligatoria para una vacuna facturable");
        if (immunizationInfoBo.getConditionId() != null &&
                !vaccineConditionApplicationStorage.isValidConditionApplication(immunizationInfoBo.getConditionId()))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_CONDITION_ID,
                    String.format("La vacuna %s tiene una condición de aplicación invalida id=%s", immunizationInfoBo.getVaccineName(),
                            immunizationInfoBo.getConditionId()));
        if (immunizationInfoBo.getSchemeId() != null && !vaccineSchemeStorage.isValidScheme(immunizationInfoBo.getSchemeId()))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_SCHEME_ID,
                    String.format("La vacuna %s tiene un esquema invalido id=%s", immunizationInfoBo.getVaccineName(),
                            immunizationInfoBo.getSchemeId()));
        validRule(immunizationInfoBo.getVaccine(), immunizationInfoBo.getConditionId(), immunizationInfoBo.getSchemeId(), immunizationInfoBo.getDose());
    }

    private void validRule(SnomedBo vaccine, Short conditionId, Short schemeId, VaccineDoseBo dose) {
        if (incompleteData(conditionId, schemeId, dose))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INCOMPLETE_DATA,
                    String.format(INCOMPLETE_DATA, conditionId, schemeId,
                            dose != null ? dose.getDescription() : null, dose != null ? dose.getOrder() : null));
        if (conditionId == null && schemeId == null && doseIsNull(dose))
            return;
        if (!vaccineRuleStorage.existRule(vaccine.getSctid(), conditionId, schemeId, dose.getDescription(), dose.getOrder()))
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_RULE,
                    String.format("La combinación de información no es una regla valida para nomivac -> " +
                                    "vacuna(sctid=%s, preferredTerm=%s)," +
                                    " condición(id=%s), esquema(id=%s), dosis(description=%s, order=%s)"
                                    , vaccine != null ? vaccine.getSctid() : null, vaccine != null ? vaccine.getPt() : null
                                    , conditionId
                                    , schemeId
                                    , dose != null ? dose.getDescription() : null
                                    , dose != null ? dose.getOrder() : null));

    }

    private boolean incompleteData(Short conditionId, Short schemeId, VaccineDoseBo dose) {
        return (!doseIsNull(dose) && (conditionId == null || schemeId == null)) ||
                (schemeId != null && (conditionId == null || doseIsNull(dose))) ||
                (conditionId != null && (schemeId == null || doseIsNull(dose)));
    }

    private void validVaccine(SnomedBo vaccine){
        if (vaccine == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_VACCINE,
                        "La información de la vacuna es obligatoria");

        if (vaccine.getSctid() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_VACCINE,
                    "El sctid code de la vacuna es obligatoria");
        if (vaccine.getPt() == null)
            throw new ImmunizationValidatorException(ImmunizationValidatorExceptionEnum.INVALID_VACCINE,
                    "El termino de preferencia de la vacuna es obligatoria");
    }

    private boolean doseIsNull(VaccineDoseBo dose) {
        return (dose == null || dose.getDescription() == null || dose.getOrder() == null);
    }
}
