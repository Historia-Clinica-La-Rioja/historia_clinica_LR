package ar.lamansys.immunization.infrastructure.input.service;

import ar.lamansys.immunization.application.fetchVaccineConditionApplicationInfo.FetchVaccineConditionApplicationInfo;
import ar.lamansys.immunization.application.fetchVaccineSchemeInfo.FetchVaccineSchemeInfo;
import ar.lamansys.immunization.domain.consultation.VaccineConsultationStorage;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.SharedImmunizationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineConditionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineConsultationInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineSchemeInfoDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SharedImmunizationPortImpl implements SharedImmunizationPort {

    private final FetchVaccineSchemeInfo fetchVaccineSchemeInfo;

    private final FetchVaccineConditionApplicationInfo fetchVaccineConditionApplicationInfo;

	private final VaccineConsultationStorage vaccineConsultationStorage;

    public SharedImmunizationPortImpl(FetchVaccineSchemeInfo fetchVaccineSchemeInfo,
									  FetchVaccineConditionApplicationInfo fetchVaccineConditionApplicationInfo, VaccineConsultationStorage vaccineConsultationStorage) {
        this.fetchVaccineSchemeInfo = fetchVaccineSchemeInfo;
        this.fetchVaccineConditionApplicationInfo = fetchVaccineConditionApplicationInfo;
		this.vaccineConsultationStorage = vaccineConsultationStorage;
	}

    @Override
    public VaccineConditionDto fetchVaccineConditionInfo(Short id) {
        VaccineConditionApplicationBo vaccineConditionApplicationBo = fetchVaccineConditionApplicationInfo.run(id);
        VaccineConditionDto result = new VaccineConditionDto();
        result.setId(id);
        result.setDescription(vaccineConditionApplicationBo.getDescription());
        return result;
    }

    @Override
    public VaccineSchemeInfoDto fetchVaccineSchemeInfo(Short id) {
        VaccineSchemeBo schemeBo = fetchVaccineSchemeInfo.run(id);
        VaccineSchemeInfoDto result = new VaccineSchemeInfoDto();
        result.setId(schemeBo.getId());
        result.setDescription(schemeBo.getDescription());
        return result;
    }

	@Override
	public List<Integer> getVaccineConsultationIdsFromPatients(List<Integer> patients) {
		return vaccineConsultationStorage.getVaccineConsultationIdsFromPatients(patients);
	}

	@Override
	public List<VaccineConsultationInfoDto> findAllVaccineConsultationByIds(List<Integer> ids) {
		return vaccineConsultationStorage.findAllByIds(ids).stream()
				.map(vc -> new VaccineConsultationInfoDto(
						vc.getId(),
						vc.getPatientId(),
						vc.getPatientMedicalCoverageId(),
						vc.getClinicalSpecialtyId(),
						vc.getInstitutionId(),
						vc.getDoctorId(),
						vc.getPerformedDate(),
						vc.getBillable()
				)).collect(Collectors.toList());
	}
}
