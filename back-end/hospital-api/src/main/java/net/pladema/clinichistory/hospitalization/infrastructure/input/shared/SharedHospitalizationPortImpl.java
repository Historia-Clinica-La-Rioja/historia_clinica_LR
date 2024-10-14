package net.pladema.clinichistory.hospitalization.infrastructure.input.shared;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationAllergyState;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.EditableDocumentDto;
import net.pladema.clinichistory.hospitalization.application.validateanestheticreport.ValidateHospitalizationAnestheticReport;
import net.pladema.clinichistory.hospitalization.domain.HospitalizationDocumentBo;
import net.pladema.clinichistory.hospitalization.infrastructure.input.shared.mapper.SharedHospitalizationMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentPatientService;

import net.pladema.patient.controller.service.PatientExternalService;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalizationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.patient.service.domain.MedicalCoverageBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

@Slf4j
@RequiredArgsConstructor
@Service
public class SharedHospitalizationPortImpl implements SharedHospitalizationPort {

	private final InternmentEpisodeService internmentEpisodeService;
	private final FetchHospitalizationAllergyState fetchHospitalizationAllergyState;
	private final InternmentPatientService internmentPatientService;
	private final PatientExternalService patientExternalService;
	private final ValidateHospitalizationAnestheticReport validateHospitalizationAnestheticReport;
	private final SharedHospitalizationMapper sharedHospitalizationMapper;

	@Override
	public Optional<ExternalPatientCoverageDto> getActiveEpisodeMedicalCoverage(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		Optional<ExternalPatientCoverageDto> result = internmentEpisodeService.getMedicalCoverage(internmentEpisodeId)
				.map(this::mapToExternalPatientCoverageDto);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<Integer> getInternmentEpisodeAllergies(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		List<Integer> result = fetchHospitalizationAllergyState.run(internmentEpisodeId)
				.stream().map(a -> a.getId()).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<Integer> getInternmentEpisodeId(Integer institutionId, Integer patientId){
		log.debug("Input parameters -> patientId {}", patientId);
		Optional<Integer> result = internmentPatientService.getInternmentEpisodeIdInProcess(institutionId, patientId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<BasicPatientDto> getPatientInfo(Integer intermentEpisodeId) {
		log.debug("Input parameters -> intermentEpisodeId {}", intermentEpisodeId);
		var result = internmentEpisodeService.getPatient(intermentEpisodeId)
				.map(patientExternalService::getBasicDataFromPatient);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public LocalDate getEntryLocalDate(Integer intermentEpisodeId) {
		log.debug("Input parameters -> intermentEpisodeId {}", intermentEpisodeId);
		var result = internmentEpisodeService.getEntryDate(intermentEpisodeId).toLocalDate();
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<Integer> getPatientMedicalCoverageId(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		Optional<Integer> result = internmentEpisodeService.getMedicalCoverage(internmentEpisodeId)
				.map(PatientMedicalCoverageBo::getId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Boolean validateHospitalizationDocument(EditableDocumentDto editableDocumentDto) {
		log.debug("Input parameters -> editableDocumentDto {}", editableDocumentDto);
		HospitalizationDocumentBo hospitalizationDocumentBo = sharedHospitalizationMapper.fromHospitalizationDocumentBo(editableDocumentDto);
		Boolean result = validateHospitalizationAnestheticReport.run(hospitalizationDocumentBo);
		log.debug("Output -> {}", result);
		return result;
	}

	private ExternalPatientCoverageDto mapToExternalPatientCoverageDto(PatientMedicalCoverageBo bo) {
		MedicalCoverageBo medicalCoverage = bo.getMedicalCoverage();
		Short condition = (bo.getCondition()!=null) ? bo.getCondition().getId() : null;
		return new ExternalPatientCoverageDto(new ExternalCoverageDto(medicalCoverage.getId(), medicalCoverage.getCuit(), bo.getPlanName(), medicalCoverage.getName(), medicalCoverage.getType()), bo.getAffiliateNumber(), bo.getActive(), bo.getVigencyDate(), condition);
	}
}
