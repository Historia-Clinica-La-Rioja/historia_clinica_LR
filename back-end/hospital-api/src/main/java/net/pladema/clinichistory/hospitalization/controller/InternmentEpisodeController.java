package net.pladema.clinichistory.hospitalization.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentTypeDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.createEpisodeDocument.CreateEpisodeDocument;
import net.pladema.clinichistory.hospitalization.application.createinternmentepisode.CreateInternmentEpisode;
import net.pladema.clinichistory.hospitalization.application.deleteEpisodeDocument.DeleteEpisodeDocument;
import net.pladema.clinichistory.hospitalization.application.getDocumentType.FetchDocumentType;
import net.pladema.clinichistory.hospitalization.application.getEpisodeDocument.FetchEpisodeDocument;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentDischargeValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentPhysicalDischargeValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.ProbableDischargeDateValid;
import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentDto;
import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentResponseDto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentEpisodeADto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentEpisodeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentSummaryDto;
import net.pladema.clinichistory.hospitalization.controller.dto.PatientDischargeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.ProbableDischargeDateDto;
import net.pladema.clinichistory.hospitalization.controller.dto.summary.InternmentEpisodeBMDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentEpisodeMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.PatientDischargeMapper;
import net.pladema.clinichistory.hospitalization.domain.InternmentEpisodeBo;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper.EpisodeDocumentDtoMapper;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentSummaryBo;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.GeneratePdfException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentEpisodeNotFoundException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.MoreThanOneConsentDocumentException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PatientNotFoundException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PersonNotFoundException;
import net.pladema.clinichistory.hospitalization.service.patientdischarge.PatientDischargeService;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.events.EHospitalApiTopicDto;
import net.pladema.events.HospitalApiPublisher;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/institutions/{institutionId}/internments")
@Tag(name = "Internment Episode", description = "Internment Episode")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class InternmentEpisodeController {

	public static final String INTERNMENT_NOT_FOUND = "internmentepisode.not.found";
	public static final String OUTPUT = "Output -> {}";
	public static final String INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID = "Input parameters -> institutionId {}, internmentEpisodeId {} ";

	private final InternmentEpisodeService internmentEpisodeService;
	private final InternmentEpisodeMapper internmentEpisodeMapper;
	private final PatientDischargeMapper patientDischargeMapper;
	private final BedExternalService bedExternalService;
	private final FeatureFlagsService featureFlagsService;
	private final PatientDischargeService patientDischargeService;
	private final LocalDateMapper localDateMapper;
	private final HospitalApiPublisher hospitalApiPublisher;
	private final FetchEpisodeDocument fetchEpisodeDocument;
	private final CreateEpisodeDocument createEpisodeDocument;
	private final FetchDocumentType fetchDocumentType;
	private final DeleteEpisodeDocument deleteEpisodeDocument;
	private final EpisodeDocumentDtoMapper mapper;
	private final CreateInternmentEpisode createInternmentEpisode;

	@PostMapping(value = "{internmentEpisodeId}/episodedocuments/{episodeDocumentTypeId}/consent/{consentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public ResponseEntity<Integer> createEpisodeDocument(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeDocumentTypeId") Integer episodeDocumentTypeId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "consentId") Integer consentId,
			@RequestPart("file") MultipartFile file) throws MoreThanOneConsentDocumentException {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, episodeDocumentTypeId {}, file {}, consentId {}",
				institutionId, internmentEpisodeId, episodeDocumentTypeId, file, consentId);
		EpisodeDocumentDto dto = new EpisodeDocumentDto(file, episodeDocumentTypeId, internmentEpisodeId, consentId);
		Integer result = createEpisodeDocument.run(dto);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("{internmentEpisodeId}/episodedocuments")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public ResponseEntity<List<EpisodeDocumentResponseDto>> getEpisodeDocuments(@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
																				@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> internmentEpisodeId {}, institutionId {}", internmentEpisodeId, institutionId);
		List<EpisodeDocumentResponseDto> result = fetchEpisodeDocument.run(internmentEpisodeId)
				.stream()
				.map(bo -> mapper.EpisodeDocumentBoToEpisodeDocumentDto(bo))
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/documentstypes")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public ResponseEntity<List<DocumentTypeDto>> getDocumentsTypes(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<DocumentTypeDto> result = fetchDocumentType.run()
				.stream()
				.map(bo -> mapper.DocumentTypeBoToDocumentTypeDto(bo))
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@DeleteMapping("/episodedocuments/{episodeDocumentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
	public ResponseEntity<Boolean> deleteDocument(@PathVariable(name = "episodeDocumentId") Integer episodeDocumentId,
								  @PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> episodeDocumentId {}, institutionId {}", episodeDocumentId, institutionId);
		Boolean result = deleteEpisodeDocument.run(episodeDocumentId);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{internmentEpisodeId}/summary")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ADMINISTRADOR_DE_CAMAS, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA, PRESCRIPTOR, ABORDAJE_VIOLENCIAS')")
	public ResponseEntity<InternmentSummaryDto> internmentEpisodeSummary(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug("Input parameters -> {}", internmentEpisodeId);
		InternmentSummaryBo internmentSummaryBo = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElse(new InternmentSummaryBo());
		InternmentSummaryDto result = internmentEpisodeMapper.toInternmentSummaryDto(internmentSummaryBo);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	public ResponseEntity<InternmentEpisodeDto> createInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestBody InternmentEpisodeADto internmentEpisodeADto) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeADto {} ", institutionId, internmentEpisodeADto);
		InternmentEpisodeBo episodeBo = internmentEpisodeMapper.toInternmentEpisodeBo(internmentEpisodeADto);
		episodeBo.setInstitutionId(institutionId);
		episodeBo = createInternmentEpisode.run(episodeBo);
		InternmentEpisodeDto result = internmentEpisodeMapper.toInternmentEpisodeDto(episodeBo);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@Transactional
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@PostMapping("/{internmentEpisodeId}/medicaldischarge")
	public ResponseEntity<PatientDischargeDto> medicalDischargeInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@InternmentDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody PatientDischargeDto patientDischargeDto) {
		log.debug("Input parameters -> internmentEpisodeId {}, PatientDischargeDto {} ", internmentEpisodeId, patientDischargeDto);
		PatientDischargeBo patientDischarge = patientDischargeMapper.toPatientDischargeBo(patientDischargeDto);
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		PatientDischargeBo patientDischargeSaved = internmentEpisodeService.saveMedicalDischarge(patientDischarge);
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeSaved);
		internmentEpisodeService.getPatient(patientDischargeSaved.getInternmentEpisodeId())
				.ifPresent( patientId -> hospitalApiPublisher.publish(patientId, institutionId,EHospitalApiTopicDto.ALTA_MEDICA) );
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@Transactional
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	@PostMapping("/{internmentEpisodeId}/administrativedischarge")
	public ResponseEntity<PatientDischargeDto> dischargeInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@InternmentDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody PatientDischargeDto patientDischargeDto) {
		log.debug("Input parameters -> internmentEpisodeId {}, PatientDischargeDto {} ", internmentEpisodeId, patientDischargeDto);
		PatientDischargeBo patientDischarge = patientDischargeMapper.toPatientDischargeBo(patientDischargeDto);
		InternmentSummaryBo internmentEpisodeSummary = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		patientDischarge.setMedicalDischargeDate(internmentEpisodeSummary.getMedicalDischargeDate());
		LocalDateTime physicalDischarge =  internmentEpisodeSummary.getPhysicalDischargeDate() != null ? internmentEpisodeSummary.getPhysicalDischargeDate() :
				patientDischarge.getAdministrativeDischargeDate();
		patientDischarge.setPhysicalDischargeDate(physicalDischarge);
		PatientDischargeBo patientDischargeSaved = internmentEpisodeService.saveAdministrativeDischarge(patientDischarge);
		if (!internmentEpisodeSummary.freeBed())
			bedExternalService.freeBed(internmentEpisodeSummary.getBedId());
		internmentEpisodeService.updateInternmentEpisodeStatus(internmentEpisodeId,
				Short.valueOf(InternmentEpisodeStatus.INACTIVE));
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeSaved);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@Transactional
	@PostMapping("/{internmentEpisodeId}/physicaldischarge")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_DE_CAMAS')")
	public ResponseEntity<PatientDischargeDto> physicalDischargeInternmentEpisode(
			@PathVariable(name="institutionId") Integer institutionId,
			@InternmentPhysicalDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {} ", internmentEpisodeId);
		InternmentSummaryBo internmentEpisodeSummary = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		PatientDischargeBo patientDischargeSaved = internmentEpisodeService.savePatientPhysicalDischarge(internmentEpisodeId);
		bedExternalService.freeBed(internmentEpisodeSummary.getBedId());
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeSaved);
		internmentEpisodeService.getPatient(patientDischargeSaved.getInternmentEpisodeId())
				.ifPresent( patientId -> hospitalApiPublisher.publish(patientId, institutionId, EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__DISCHARGE__PHYSIC) );
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{internmentEpisodeId}/minDischargeDate")
	public DateTimeDto getMinDischargeDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
		log.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		if (this.featureFlagsService.isOn(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)) {
			return localDateMapper.toDateTimeDto(internmentEpisodeService.getLastUpdateDateOfInternmentEpisode(internmentEpisodeId));
		}
		PatientDischargeBo patientDischarge =  patientDischargeService.getPatientDischarge(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		LocalDateTime result = patientDischarge.getMedicalDischargeDate();
		log.debug(OUTPUT, result);
		return localDateMapper.toDateTimeDto(result);
	}

	@GetMapping("/{internmentEpisodeId}")
	public ResponseEntity<InternmentEpisodeBMDto> getInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		InternmentEpisode internmentEpisode = internmentEpisodeService.getInternmentEpisode(internmentEpisodeId,institutionId);
		InternmentEpisodeBMDto result = internmentEpisodeMapper.toInternmentEpisodeBMDto(internmentEpisode);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{internmentEpisodeId}/patientdischarge")
	public ResponseEntity<PatientDischargeDto> getPatientDischarge(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		PatientDischargeBo patientDischargeBo =	patientDischargeService.getPatientDischarge(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeBo);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{internmentEpisodeId}/lastupdatedate")
	public DateTimeDto getLastUpdateDateOfInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		LocalDateTime result = internmentEpisodeService.getLastUpdateDateOfInternmentEpisode(internmentEpisodeId);
		log.debug(OUTPUT, result);
		return localDateMapper.toDateTimeDto(result);
	}

	@ProbableDischargeDateValid
	@PutMapping("/{internmentEpisodeId}/probabledischargedate")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
	public ResponseEntity<DateTimeDto> updateProbableDischargeDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody ProbableDischargeDateDto probableDischargeDateDto) {
		log.debug("Input parameters -> institutionId {}, intermentEpisodeId {} ", institutionId, internmentEpisodeId);
		LocalDateTime probableDischargeDate = localDateMapper.fromStringToLocalDateTime(probableDischargeDateDto.getProbableDischargeDate());
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_CARGA_FECHA_PROBABLE_ALTA))
			return new ResponseEntity<>(localDateMapper.toDateTimeDto(probableDischargeDate), HttpStatus.BAD_REQUEST);
		LocalDateTime result = internmentEpisodeService.updateInternmentEpisodeProbableDischargeDate(internmentEpisodeId, probableDischargeDate);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(localDateMapper.toDateTimeDto(result));
	}

	@GetMapping("/{internmentEpisodeId}/episode-document-type/{consentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<Resource> generateEpisodeDocumentType(
			@RequestParam(value = "procedures", required = false) List<String> procedures,
			@RequestParam(value = "observations", required = false) String observations,
			@RequestParam(value = "professionalId", required = false) String professionalId,
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "consentId") Integer consentId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) throws GeneratePdfException, InternmentEpisodeNotFoundException, PersonNotFoundException, PatientNotFoundException {
		log.debug("Input parameters -> institutionId {}, consentId {}, intermentEpisodeId {}, procedures {}, observations {}, professionalId {}", institutionId, consentId, internmentEpisodeId, procedures, observations, professionalId);
		var result = internmentEpisodeService.generateEpisodeDocumentType(institutionId, consentId, internmentEpisodeId, procedures, observations, professionalId);

		log.debug(OUTPUT, result);
		return StoredFileResponse.sendGeneratedBlob(//InternmentEpisodeDocumentService.generateConsentDocument
				result
		);
	}
}