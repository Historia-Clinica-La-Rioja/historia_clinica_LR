package ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.publicapi.apisumar.domain.ImmunizationsDetailBo;
import ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.dto.ImmunizationsDetailDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImmunizationsMapper {

	private final LocalDateMapper localDateMapper;

	public ImmunizationsMapper(LocalDateMapper localDateMapper) {
		this.localDateMapper = localDateMapper;
	}

	public List<ImmunizationsDetailDto> mapToImmunizations(List<ImmunizationsDetailBo> immunizationsDetailBoList) {
		return immunizationsDetailBoList.stream()
				.map(this::mapToImmunizations)
				.collect(Collectors.toList());
	}

	private ImmunizationsDetailDto mapToImmunizations(ImmunizationsDetailBo immunizationsDetailBo) {
		return ImmunizationsDetailDto.builder()
				.institution(immunizationsDetailBo.getInstitution())
				.operativeUnit(immunizationsDetailBo.getOperativeUnit())
				.lender(immunizationsDetailBo.getLender())
				.lenderIdentificationNumber(immunizationsDetailBo.getLenderIdentificationNumber())
				.attentionDate(immunizationsDetailBo.getAttentionDate())
				.patientIdentificationNumber(immunizationsDetailBo.getPatientIdentificationNumber())
				.patientName(immunizationsDetailBo.getPatientName())
				.patientSex(immunizationsDetailBo.getPatientSex())
				.patientGender(immunizationsDetailBo.getPatientGender())
				.patientSelfPerceivedName(immunizationsDetailBo.getPatientSelfPerceivedName())
				.patientBirthDate(immunizationsDetailBo.getPatientBirthDate())
				.patientAgeTurn(immunizationsDetailBo.getPatientAgeTurn())
				.patientAge(immunizationsDetailBo.getPatientAge())
				.ethnicity(immunizationsDetailBo.getEthnicity())
				.medicalCoverage(immunizationsDetailBo.getMedicalCoverage())
				.address(immunizationsDetailBo.getAddress())
				.location(immunizationsDetailBo.getLocation())
				.instructionLevel(immunizationsDetailBo.getInstructionLevel())
				.workSituation(immunizationsDetailBo.getWorkSituation())
				.vaccine(immunizationsDetailBo.getVaccine())
				.dosage(immunizationsDetailBo.getDosage())
				.lotNumber(immunizationsDetailBo.getLotNumber())
				.note(immunizationsDetailBo.getNote())
				.scheme(immunizationsDetailBo.getScheme())
				.vaccineScheme(immunizationsDetailBo.getVaccineScheme())
				.applicationCondition(immunizationsDetailBo.getApplicationCondition())
				.evolution(immunizationsDetailBo.getEvolution())
				.build();
	}
}
