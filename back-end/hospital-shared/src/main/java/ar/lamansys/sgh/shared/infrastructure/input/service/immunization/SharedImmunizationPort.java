package ar.lamansys.sgh.shared.infrastructure.input.service.immunization;

public interface SharedImmunizationPort {

    VaccineConditionDto fetchVaccineConditionInfo(Short id);

    VaccineSchemeInfoDto fetchVaccineSchemeInfo(Short id);

}
