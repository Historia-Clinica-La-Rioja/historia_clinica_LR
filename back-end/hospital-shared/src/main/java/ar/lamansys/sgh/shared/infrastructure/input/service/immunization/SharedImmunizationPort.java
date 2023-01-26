package ar.lamansys.sgh.shared.infrastructure.input.service.immunization;

import java.util.List;

public interface SharedImmunizationPort {

    VaccineConditionDto fetchVaccineConditionInfo(Short id);

    VaccineSchemeInfoDto fetchVaccineSchemeInfo(Short id);

	List<Integer> getVaccineConsultationIdsFromPatients(List<Integer> patients);
	List<VaccineConsultationInfoDto> findAllVaccineConsultationByIds(List<Integer> ids);

}
