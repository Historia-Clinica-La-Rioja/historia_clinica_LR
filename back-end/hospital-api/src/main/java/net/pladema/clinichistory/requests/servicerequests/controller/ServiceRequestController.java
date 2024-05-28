package net.pladema.clinichistory.requests.servicerequests.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.StudyOrderReportInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.StudyWithoutOrderReportInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceRequestDto;
import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.servicerequests.application.AddDiagnosticReportObservations;
import net.pladema.clinichistory.requests.servicerequests.application.CreateServiceRequestPdf;
import net.pladema.clinichistory.requests.servicerequests.application.GetDiagnosticReportObservationGroup;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.CompleteRequestDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.DiagnosticReportInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.DiagnosticReportInfoWithFilesDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderReportInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyWithoutOrderReportInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.observations.AddDiagnosticReportObservationsCommandDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.observations.GetDiagnosticReportObservationGroupDto;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.CompleteDiagnosticReportMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.CreateServiceRequestMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.DiagnosticReportInfoMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.DiagnosticReportObservationsMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.FileMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyOrderReportInfoMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyWithoutOrderReportInfoMapper;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.GetDiagnosticReportObservationGroupBo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.AddObservationsCommandVo;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportObservationException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.InvalidProcedureTemplateChangeException;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportRDIService;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.DeleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.DiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.ListDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.ListStudyOrderReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.ListStudyWithoutOrderReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.UpdateDiagnosticReportFileService;
import net.pladema.clinichistory.requests.servicerequests.service.UploadDiagnosticReportCompletedFileService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.events.EHospitalApiTopicDto;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.patient.controller.mapper.PatientMedicalCoverageMapper;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;


@Tag(name = "Service request", description = "Service request")
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/service-requests")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ServiceRequestController {
    private static final String OUTPUT = "create result -> {}";
    private static final String COMMON_INPUT = "Input parameters -> institutionId {} patientId {}, diagnosticReportId {}";

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
    private final CreateServiceRequestService createServiceRequestService;
    private final CreateServiceRequestMapper createServiceRequestMapper;
    private final PatientExternalService patientExternalService;
    private final StudyMapper studyMapper;
    private final ListDiagnosticReportInfoService listDiagnosticReportInfoService;
	private final ListStudyOrderReportInfoService listStudyOrderReportInfoService;
	private final ListStudyWithoutOrderReportInfoService listStudyWithoutOrderReportInfoService;
    private final DiagnosticReportInfoMapper diagnosticReportInfoMapper;
	private final StudyOrderReportInfoMapper studyOrderReportInfoMapper;
	private final StudyWithoutOrderReportInfoMapper studyWithoutOrderReportInfoMapper;
    private final DeleteDiagnosticReportService deleteDiagnosticReportService;
    private final CompleteDiagnosticReportService completeDiagnosticReportService;
	private final CompleteDiagnosticReportRDIService completeDiagnosticReportRDIService;
    private final CompleteDiagnosticReportMapper completeDiagnosticReportMapper;
    private final UploadDiagnosticReportCompletedFileService uploadDiagnosticReportCompletedFileService;
    private final UpdateDiagnosticReportFileService updateDiagnosticReportFileService;
    private final DiagnosticReportInfoService diagnosticReportInfoService;
    private final FileMapper fileMapper;
	private final HospitalApiPublisher hospitalApiPublisher;
	private final PatientMedicalCoverageService patientMedicalCoverageService;
	private final PatientMedicalCoverageMapper patientMedicalCoverageMapper;
	private final CreateServiceRequestPdf createServiceRequestPdf;
	private final SharedReferenceCounterReference sharedReferenceCounterReference;
	private final GetDiagnosticReportObservationGroup getDiagnosticReportObservationGroup;
	private final AddDiagnosticReportObservations addDiagnosticReportObservations;
	private final DiagnosticReportObservationsMapper diagnosticReportObservationsMapper;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public List<Integer> create(@PathVariable(name = "institutionId") Integer institutionId,
                                @PathVariable(name = "patientId") Integer patientId,
                                @RequestBody @Valid PrescriptionDto serviceRequestListDto
    ) {
        log.debug("Input parameters -> institutionId {} patientId {}, ServiceRequestListDto {}", institutionId, patientId, serviceRequestListDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        Map<String, List<PrescriptionItemDto>> srGroupBy = serviceRequestListDto.getItems().stream()
                .collect(Collectors.groupingBy(PrescriptionItemDto::getCategoryId));

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);

        ArrayList<Integer> result = new ArrayList<>();

        srGroupBy.forEach((categoryId, studyListDto) -> {
            ServiceRequestBo serviceRequestBo = createServiceRequestMapper.parseTo(
                    studyMapper,
                    doctorId,
                    patientDto,
                    categoryId,
                    serviceRequestListDto.getMedicalCoverageId(),
                    studyListDto);
            serviceRequestBo.setInstitutionId(institutionId);
            Integer srId = createServiceRequestService.execute(serviceRequestBo);
			hospitalApiPublisher.publish(serviceRequestBo.getPatientId(), institutionId, EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST);
            result.add(srId);
        });

        log.debug(OUTPUT, result);
        return result;
    }

    @PutMapping("/{diagnosticReportId}/complete")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO')")
    public void complete(@PathVariable(name = "institutionId") Integer institutionId,
                         @PathVariable(name = "patientId") Integer patientId,
                         @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId,
                         @RequestBody() CompleteRequestDto completeRequestDto) {

        log.debug("Input parameters ->  {} institutionIdpatientId {}, diagnosticReportId {}, completeRequestDto {}",
                institutionId,
                patientId,
                diagnosticReportId,
                completeRequestDto);

		if (completeRequestDto.getReferenceClosure() != null)
			completeRequestDto.getReferenceClosure().setFileIds(completeRequestDto.getFileIds());

		CompleteDiagnosticReportBo completeDiagnosticReportBo = completeDiagnosticReportMapper.parseTo(completeRequestDto);
		Integer result = completeDiagnosticReportService.run(patientId, diagnosticReportId, completeDiagnosticReportBo, institutionId);
        updateDiagnosticReportFileService.run(result, completeRequestDto.getFileIds());
        log.debug(OUTPUT, result);
    }

	@PutMapping("/{appointmentId}/completeByRDI")
	@Transactional
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, TECNICO')")
	public void completeByRDI(@PathVariable(name = "institutionId") Integer institutionId,
						 @PathVariable(name = "patientId") Integer patientId,
						 @PathVariable(name = "appointmentId") Integer appointmentId) {
		log.debug("Input parameters -> institutionId {}, patientId {}, appointmentId {}", institutionId, patientId, appointmentId);
		Integer result = completeDiagnosticReportRDIService.run(patientId, appointmentId);
		log.debug(OUTPUT, result);
	}

    @PostMapping(value = "/{diagnosticReportId}/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO')")
    public List<Integer> uploadFile(@PathVariable(name = "institutionId") Integer institutionId,
                                    @PathVariable(name = "patientId") Integer patientId,
                                    @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId,
                                    @RequestPart("files") MultipartFile[] files) {
        log.debug("Input parameters ->  {} institutionIdpatientId {}, diagnosticReportId {}",
                institutionId,
                patientId,
                diagnosticReportId);
        var result = uploadDiagnosticReportCompletedFileService.execute(files, diagnosticReportId, patientId);
        log.debug(OUTPUT, result);
        return result;
    }


    @DeleteMapping("/{diagnosticReportId}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public void delete(@PathVariable(name = "institutionId") Integer institutionId,
                       @PathVariable(name = "patientId") Integer patientId,
                       @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId
    ) {
        log.debug(COMMON_INPUT, institutionId, patientId, diagnosticReportId);
        deleteDiagnosticReportService.execute(patientId, diagnosticReportId);
    }

    @GetMapping("/{diagnosticReportId}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO')")
    public DiagnosticReportInfoWithFilesDto get(@PathVariable(name = "institutionId") Integer institutionId,
                                                @PathVariable(name = "patientId") Integer patientId,
                                                @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId
    ) {
        log.debug(COMMON_INPUT, institutionId, patientId, diagnosticReportId);


        DiagnosticReportBo resultService = diagnosticReportInfoService.run(diagnosticReportId);
        ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalByUserId(resultService.getUserId());
		ReferenceRequestDto reference = sharedReferenceCounterReference.getReferenceByServiceRequestId(resultService.getEncounterId()).orElse(null);
		DiagnosticReportInfoDto driDto = diagnosticReportInfoMapper.parseTo(
			resultService,
			professionalDto,
			reference
			);
        DiagnosticReportInfoWithFilesDto result = new DiagnosticReportInfoWithFilesDto(
                driDto,
                fileMapper.parseToList(resultService.getFiles()));
        log.debug(OUTPUT, result);
        return result;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public List<DiagnosticReportInfoDto> getList(@PathVariable(name = "institutionId") Integer institutionId,
                                                 @PathVariable(name = "patientId") Integer patientId,
                                                 @RequestParam(value = "statusId", required = false) String status,
                                                 @RequestParam(value = "study", required = false) String study,
                                                 @RequestParam(value = "category", required = false) String category,
                                                 @RequestParam(value = "healthCondition", required = false) String healthCondition,
												 @RequestParam(value = "categoriesToBeExcluded", required = false) List<String> categoriesToBeExcluded) {
        log.debug("Input parameters -> institutionId {}, patientId {}, status {}, diagnosticReport {}, healthCondition {}, " +
						"category {}, categoriesToBeExcluded {}",
                institutionId, patientId, status, study, healthCondition, category, categoriesToBeExcluded);

        List<DiagnosticReportBo> resultService = listDiagnosticReportInfoService.getListIncludingConfidential(new DiagnosticReportFilterBo(
                patientId, status, study, healthCondition, category, categoriesToBeExcluded), institutionId);

        List<DiagnosticReportInfoDto> result = resultService.stream()
                .map(diagnosticReportBo -> {
                    ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalByUserId(diagnosticReportBo.getUserId());
					PatientMedicalCoverageBo coverage = patientMedicalCoverageService.getActiveCoveragesByOrderId(diagnosticReportBo.getEncounterId());
					ReferenceRequestDto reference = sharedReferenceCounterReference.getReferenceByServiceRequestId(diagnosticReportBo.getEncounterId()).orElse(null);
                    return diagnosticReportInfoMapper.parseTo(
                    	diagnosticReportBo,
                    	professionalDto,
                    	patientMedicalCoverageMapper.toPatientMedicalCoverageDto(coverage),
                    	reference
                    	);
				})
                .collect(Collectors.toList());

        log.trace(OUTPUT, result);
        return result;
    }

	@GetMapping("/medicalOrders")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
	public List<DiagnosticReportInfoDto> getMedicalOrderList(@PathVariable(name = "institutionId") Integer institutionId,
												 @PathVariable(name = "patientId") Integer patientId,
												 @RequestParam(value = "statusId", required = false) String status,
												 @RequestParam(value = "category", required = false) String category) {
		log.debug("Input parameters -> institutionId {}, patientId {}, status {}, category {}",
				institutionId, patientId, status, category);

		List<DiagnosticReportBo> resultService = listDiagnosticReportInfoService.getMedicalOrderList(new DiagnosticReportFilterBo(
				patientId, status, null, null, category, null));

		List<DiagnosticReportInfoDto> result = resultService.stream()
				.map(diagnosticReportBo -> {
					ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalByUserId(diagnosticReportBo.getUserId());
					PatientMedicalCoverageBo coverage = patientMedicalCoverageService.getActiveCoveragesByOrderId(diagnosticReportBo.getEncounterId());
					return diagnosticReportInfoMapper.parseTo(
						diagnosticReportBo,
						professionalDto,
						patientMedicalCoverageMapper.toPatientMedicalCoverageDto(coverage),
						null);
				})
				.collect(Collectors.toList());

		log.trace(OUTPUT, result);
		return result;
	}

	@GetMapping("/studyOrder")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	public List<StudyOrderReportInfoDto> getListStudyOrder(@PathVariable(name = "institutionId") Integer institutionId,
																 @PathVariable(name = "patientId") Integer patientId) {
		log.trace("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);

		List<StudyOrderReportInfoBo> resultService = listStudyOrderReportInfoService.getListStudyOrder(patientId);

		List<StudyOrderReportInfoDto> result = resultService.stream()
				.map(studyOrderReportInfoBo -> {
					ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalByUserId(studyOrderReportInfoBo.getDoctorUserId());
					return studyOrderReportInfoMapper.parseToDto(studyOrderReportInfoBo, professionalDto);
				})
				.collect(Collectors.toList());

		log.trace(OUTPUT, result);
		return result;
	}

	@GetMapping("/studyWithoutOrder")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	public List<StudyWithoutOrderReportInfoDto> getListStudyWithoutOrder(@PathVariable(name = "institutionId") Integer institutionId,
																		 @PathVariable(name = "patientId") Integer patientId) {
		log.debug("Input parameters -> patientId {}", patientId);

		List<StudyWithoutOrderReportInfoBo> resultService = listStudyWithoutOrderReportInfoService.execute(patientId);

		List<StudyWithoutOrderReportInfoDto> result = resultService.stream()
				.map(studyWithoutOrderReportInfoMapper::parseTo)
				.collect(Collectors.toList());

		log.trace(OUTPUT, result);
		return result;
	}

    @GetMapping(value = "/{serviceRequestId}/download-pdf")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, TECNICO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public ResponseEntity<Resource> downloadPdf(@PathVariable(name = "institutionId") Integer institutionId,
												@PathVariable(name = "patientId") Integer patientId,
												@PathVariable(name = "serviceRequestId") Integer serviceRequestId) throws PDFDocumentException {
        log.trace("Input parameters -> institutionId {}, patientId {}, serviceRequestId {}", institutionId, patientId, serviceRequestId);
		var result = createServiceRequestPdf.run(institutionId, patientId, serviceRequestId);
		log.trace(OUTPUT, result);

		return StoredFileResponse.sendGeneratedBlob(//ServiceRequestService.downloadPdf
				result
		);
    }

	@PutMapping("/{diagnosticReportId}/observations")
	@Transactional
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO')")
	public void addObservations(@PathVariable(name = "institutionId") Integer institutionId,
						 @PathVariable(name = "patientId") Integer patientId,
						 @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId,
						 @RequestBody AddDiagnosticReportObservationsCommandDto observations
						 ) throws InvalidProcedureTemplateChangeException, DiagnosticReportObservationException {
		log.debug("Input parameters ->  {} institutionIdpatientId {}, diagnosticReportId {}, observations {}",
				institutionId,
				patientId,
				diagnosticReportId,
				observations);

		AddObservationsCommandVo observationsBo = diagnosticReportObservationsMapper.fromDto(observations);
		addDiagnosticReportObservations.run(diagnosticReportId, observationsBo, institutionId, patientId);

		log.debug(OUTPUT, "endpoint doesn't provide output");
	}

	@GetMapping("/{diagnosticReportId}/observations")
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO')")
	public GetDiagnosticReportObservationGroupDto getObservations(
		@PathVariable(name = "institutionId") Integer institutionId,
		@PathVariable(name = "patientId") Integer patientId,
		@PathVariable(name = "diagnosticReportId") Integer diagnosticReportId
	) {
		log.debug(COMMON_INPUT, institutionId, patientId, diagnosticReportId);

		GetDiagnosticReportObservationGroupBo observations =
			getDiagnosticReportObservationGroup.run(diagnosticReportId);

		GetDiagnosticReportObservationGroupDto result = diagnosticReportObservationsMapper.parseTo(observations);

		log.debug(OUTPUT, result);
		return result;
	}

}
