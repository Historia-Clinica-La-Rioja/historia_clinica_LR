package ar.lamansys.immunization.domain.vaccine;

public interface VaccineRuleStorage {

    boolean existRule(String sctid, Short conditionApplicationId, Short schemeId, String dose, Short doseOrder);
}
