package ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.publicapi.apisumar.domain.ConsultationDetailDataBo;
import ar.lamansys.sgh.publicapi.apisumar.domain.exceptions.ConsultationNotFoundException;
import ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.dto.ConsultationDetailDataDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConsultationMapper {

	private final LocalDateMapper localDateMapper;

	public ConsultationMapper(LocalDateMapper localDateMapper) {
		this.localDateMapper = localDateMapper;
	}

	public List<ConsultationDetailDataDto> mapToConsultations(List<ConsultationDetailDataBo> consultationDetailDataBoList) {
		return consultationDetailDataBoList.stream()
				.map(this::mapToConsultations)
				.collect(Collectors.toList());
	}

	private ConsultationDetailDataDto mapToConsultations(ConsultationDetailDataBo consultationDetailDataBo) {
		return ConsultationDetailDataDto.builder()
				.institution(consultationDetailDataBo.getInstitution())
				.origin(consultationDetailDataBo.getOrigin())
				.operativeUnit(consultationDetailDataBo.getOperativeUnit())
				.lender(consultationDetailDataBo.getLender())
				.lenderIdentificationNumber(consultationDetailDataBo.getLenderIdentificationNumber())
				.attentionDate(consultationDetailDataBo.getAttentionDate())
				.patientIdentificationNumber(consultationDetailDataBo.getPatientIdentificationNumber())
				.patientName(consultationDetailDataBo.getPatientName())
				.patientSex(consultationDetailDataBo.getPatientSex())
				.patientGender(consultationDetailDataBo.getPatientGender())
				.patientSelfPerceivedName(consultationDetailDataBo.getPatientSelfPerceivedName())
				.patientBirthDate(consultationDetailDataBo.getPatientBirthDate())
				.patientAgeTurn(consultationDetailDataBo.getPatientAgeTurn())
				.patientAge(consultationDetailDataBo.getPatientAge())
				.ethnicity(consultationDetailDataBo.getEthnicity())
				.medicalCoverage(consultationDetailDataBo.getMedicalCoverage())
				.address(consultationDetailDataBo.getAddress())
				.location(consultationDetailDataBo.getLocation())
				.instructionLevel(consultationDetailDataBo.getInstructionLevel())
				.workSituation(consultationDetailDataBo.getWorkSituation())
				.systolicPressure(consultationDetailDataBo.getSystolicPressure())
				.diastolicPressure(consultationDetailDataBo.getDiastolicPressure())
				.meanArterialPressure(consultationDetailDataBo.getMeanArterialPressure())
				.temperature(consultationDetailDataBo.getTemperature())
				.heartRate(consultationDetailDataBo.getHeartRate())
				.respiratoryRate(consultationDetailDataBo.getRespiratoryRate())
				.bloodOxygenSaturation(consultationDetailDataBo.getBloodOxygenSaturation())
				.height(consultationDetailDataBo.getHeight())
				.weight(consultationDetailDataBo.getWeight())
				.bmi(consultationDetailDataBo.getBmi())
				.headCircumference(consultationDetailDataBo.getHeadCircumference())
				.reasons(consultationDetailDataBo.getReasons())
				.procedures(consultationDetailDataBo.getProcedures())
				.dentalProcedures(consultationDetailDataBo.getDentalProcedures())
				.cpo(consultationDetailDataBo.getCpo())
				.ceo(consultationDetailDataBo.getCeo())
				.problems(consultationDetailDataBo.getProblems())
				.medication(consultationDetailDataBo.getMedication())
				.evolution(consultationDetailDataBo.getEvolution())
				.build();
	}

}
