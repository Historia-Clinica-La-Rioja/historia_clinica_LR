package net.pladema.clinichistory.outpatient.createoutpatient.controller;

import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionNewConsultationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ConsultationResponseDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.DocumentAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.outpatient.application.markaserroraproblem.CanBeMarkAsError;
import net.pladema.clinichistory.outpatient.application.markaserroraproblem.MarkAsErrorAProblem;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.CreateOutpatientServiceRequestDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientUpdateImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper.OutpatientConsultationMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.mapper.ServiceRequestToFileMapper;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientConsultationService;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.SharedCreateConsultationServiceRequest;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientDocumentService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason.OutpatientReasonService;
import net.pladema.clinichistory.outpatient.domain.ProblemErrorBo;
import net.pladema.clinichistory.outpatient.infrastructure.input.dto.ErrorProblemDto;
import net.pladema.clinichistory.outpatient.infrastructure.input.dto.ProblemInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.DiagnosticReportObservationsMapper;
import net.pladema.medicalconsultation.appointment.controller.service.AppointmentExternalService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/outpatient/consultations")
public class OutpatientConsultationController implements OutpatientConsultationAPI {

    private static final Logger LOG = LoggerFactory.getLogger(OutpatientConsultationController.class);
    public static final String OUTPUT = "Output -> {}";

    private final CreateOutpatientConsultationService createOutpatientConsultationService;

    private final CreateOutpatientDocumentService createOutpatientDocumentService;

    private final OutpatientReasonService outpatientReasonService;

    private final HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService;

    private final OutpatientConsultationMapper outpatientConsultationMapper;

    private final AppointmentExternalService appointmentExternalService;

    private final DateTimeProvider dateTimeProvider;

    private final PatientExternalService patientExternalService;

    @Value("${test.stress.disable.validation:false}")
    private boolean disableValidation;

	private final SharedAppointmentPort sharedAppointmentPort;

    private final SharedReferenceCounterReference sharedReferenceCounterReference;

    private final MarkAsErrorAProblem markAsErrorAProblem;

    private final CanBeMarkAsError canBeMarkAsError;

    private final SharedCreateConsultationServiceRequest sharedCreateConsultationServiceRequest;

    private final DiagnosticReportObservationsMapper diagnosticReportObservationsMapper;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
    public ResponseEntity<ConsultationResponseDto> createOutpatientConsultation(
            Integer institutionId,
            Integer patientId,
            @RequestPart("createOutpatientDto") CreateOutpatientDto createOutpatientDto,
			@RequestPart(value = "serviceRequestFiles", required = false) MultipartFile[] serviceRequestFiles
	) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, createOutpatientDto {}", institutionId, patientId, createOutpatientDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());

		Integer patientMedicalCoverageId = createOutpatientDto.getPatientMedicalCoverageId();
		//Find the associated appointments and get the OS
		if (patientMedicalCoverageId == null)
			patientMedicalCoverageId= appointmentExternalService.getMedicalCoverage(patientId, doctorId);

        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, true,
                createOutpatientDto.getClinicalSpecialtyId(), patientMedicalCoverageId, createOutpatientDto.getHierarchicalUnitId());

        OutpatientDocumentBo outpatient = outpatientConsultationMapper.fromCreateOutpatientDto(createOutpatientDto);
        outpatient.setEncounterId(newOutPatient.getId());
        outpatient.setInstitutionId(institutionId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        outpatient.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));
        outpatient.setPatientId(patientId);
		outpatient.setMedicalCoverageId(patientMedicalCoverageId);

        List<ReasonBo> reasons = outpatientConsultationMapper.fromListReasonDto(createOutpatientDto.getReasons());
        outpatient.setReasons(reasons);
        outpatientReasonService.addReasons(newOutPatient.getId(), reasons);

        Long documentId = createOutpatientDocumentService.execute(outpatient, true).getId();
		Integer appointmentId = null;
        if (!disableValidation && (appointmentExternalService.hasCurrentAppointment(patientId,doctorId,dateTimeProvider.nowDate())
				|| appointmentExternalService.hasOldAppointment(patientId,doctorId))) {
			appointmentId = appointmentExternalService.serveAppointment(patientId, doctorId, dateTimeProvider.nowDate());
		}
		if(appointmentId != null)
			this.sharedAppointmentPort.saveDocumentAppointment(new DocumentAppointmentDto(documentId, appointmentId));

		List<Integer> orderIds = new ArrayList<>();
        if (!createOutpatientDto.getReferences().isEmpty()) {
			orderIds = sharedReferenceCounterReference.saveReferences(mapToCompleteReferenceDto(createOutpatientDto.getReferences(), institutionId,
					doctorId, patientMedicalCoverageId, patientId, newOutPatient.getId()));
        }

		/**
		 * Create the service requests
		 * Before, service requests were created for each procedure. Now the input dto has an array of procedures
		 * and another one of service request.
		 */
		List<Integer> orderIdsFromServiceRequests = createServiceRequest(
			doctorId, createOutpatientDto.getServiceRequests(), patientMedicalCoverageId, patientDto, institutionId,
			newOutPatient.getId(), toList(serviceRequestFiles));
		orderIds.addAll(orderIdsFromServiceRequests);

		ConsultationResponseDto result = new ConsultationResponseDto(newOutPatient.getId(), orderIds);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

	private List<MultipartFile> toList(MultipartFile[] serviceRequestFiles) {
		if (serviceRequestFiles == null) return Collections.emptyList();
		return Arrays.asList(serviceRequestFiles);
	}

	/**
	 * Try to create a service request (and a diagnostic report with its observations) for each requested
	 * service request
	 */
	private List<Integer> createServiceRequest(
		Integer doctorId,
		List<@Valid CreateOutpatientServiceRequestDto> serviceRequests,
		Integer medicalCoverageId,
		BasicPatientDto patientDto,
		Integer institutionId,
		Integer newOutpatientConsultationId,
		List<MultipartFile> serviceRequestFiles
	) {
		List<Integer> orderIds = new ArrayList<>();
		Map<CreateOutpatientServiceRequestDto, List<MultipartFile>> requestFiles = ServiceRequestToFileMapper.
			buildRequestFilesMap(serviceRequests, serviceRequestFiles);
		for (int i = 0; i < serviceRequests.size(); i++) {
			CreateOutpatientServiceRequestDto serviceRequest = serviceRequests.get(i);
			if (serviceRequest != null) {

				String categoryId = serviceRequest.getCategoryId();
				String healthConditionSctid = serviceRequest.getHealthConditionSctid();
				String healthConditionPt = serviceRequest.getHealthConditionPt();
				Boolean createWithStatusFinal = serviceRequest.getCreationStatus().isFinal();
				Optional<SharedAddObservationsCommandVo> addObservationsCommand = Optional
					.ofNullable(serviceRequest.getObservations())
					.map(diagnosticReportObservationsMapper::fromDto);

				Integer patientId = patientDto.getId();
				Short patientGenderId = patientDto.getPerson().getGender().getId();
				Short patientAge = patientDto.getPerson().getAge();
				String sctid = serviceRequest.getSnomedSctid();
				String pt = serviceRequest.getSnomedPt();
				List<MultipartFile> files = requestFiles.get(serviceRequest);
				String textObservation = serviceRequest.getObservation();

				Integer orderId = sharedCreateConsultationServiceRequest.createOutpatientServiceRequest(
					doctorId, categoryId, institutionId,
					healthConditionSctid, healthConditionPt, medicalCoverageId, newOutpatientConsultationId,
					sctid, pt, createWithStatusFinal, addObservationsCommand, patientId, patientGenderId, patientAge,
					files, textObservation
				);
				orderIds.add(orderId);
			}
		}

		return orderIds;
	}

	@Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO')")
    public ResponseEntity<Boolean> gettingVaccine(
            Integer institutionId,
            Integer patientId,
            List<OutpatientImmunizationDto> vaccineDto) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, OutpatientImmunizationDto {}", institutionId, patientId, vaccineDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        Integer clinicalSpecialtyId = getClinicalSpecialtyId(vaccineDto);

        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, true, clinicalSpecialtyId, null, null);
        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setEncounterId(newOutPatient.getId());
        outpatient.setInstitutionId(institutionId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        outpatient.setPatientId(patientId);
        outpatient.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));

        outpatient.setEvolutionNote(extractNotes(vaccineDto));
        outpatient.setImmunizations(extractImmunizations(vaccineDto,institutionId));

        outpatient.setClinicalSpecialtyId(clinicalSpecialtyId);

        createOutpatientDocumentService.execute(outpatient, true);


        if (!disableValidation && appointmentExternalService.hasCurrentAppointment(patientId,doctorId,dateTimeProvider.nowDate()))
            appointmentExternalService.serveAppointment(patientId, doctorId, dateTimeProvider.nowDate());
        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
    }

    private List<ImmunizationBo> extractImmunizations(List<OutpatientImmunizationDto> vaccineDto, Integer institutionId) {
        List<ImmunizationBo> immunizationBos = vaccineDto.stream()
                .map(outpatientConsultationMapper::fromOutpatientImmunizationDto)
                .collect(Collectors.toList());

        immunizationBos.forEach(immunizationBo -> immunizationBo.setInstitutionId(institutionId));

        return immunizationBos;
    }

    private String extractNotes(List<OutpatientImmunizationDto> vaccineDto) {
        return vaccineDto.stream()
                .map(OutpatientImmunizationDto::getNote)
                .collect(Collectors.toList()).get(0);
    }

    private Integer getClinicalSpecialtyId(List<OutpatientImmunizationDto> vaccineDto) {
        return vaccineDto.stream()
                .map(OutpatientImmunizationDto::getClinicalSpecialtyId)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<Boolean> updateImmunization(
            Integer institutionId,
            Integer patientId,
            OutpatientUpdateImmunizationDto outpatientUpdateImmunization) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, OutpatientImmunizationDto {}", institutionId, patientId, outpatientUpdateImmunization);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, false, null, null, null);

        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setEncounterId(newOutPatient.getId());
        outpatient.setInstitutionId(institutionId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        outpatient.setPatientId(patientId);
        outpatient.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));

        ImmunizationBo immunizationBo = outpatientConsultationMapper.fromOutpatientImmunizationDto(outpatientUpdateImmunization);
        immunizationBo.setInstitutionId(institutionId);
        outpatient.setImmunizations(Collections.singletonList(immunizationBo));

        createOutpatientDocumentService.execute(outpatient, true);

        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
    public ResponseEntity<Boolean> solveHealthCondition(
            Integer institutionId,
            Integer patientId,
            @Valid HealthConditionNewConsultationDto solvedProblemDto) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, HealthConditionNewConsultationDto {}", institutionId, patientId, solvedProblemDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, false, null, null, null);
        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setEncounterId(newOutPatient.getId());
        outpatient.setInstitutionId(institutionId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        outpatient.setPatientId(patientId);
        outpatient.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));

        outpatient.setProblems(Collections.singletonList(outpatientConsultationMapper.fromHealthConditionNewConsultationDto(solvedProblemDto)));
        outpatient.getProblems().forEach(p->p.setId(null));
		outpatient.setFamilyHistories(new ReferableItemBo<>(new ArrayList<>(), null));
		outpatient.setAllergies(new ReferableItemBo<>(new ArrayList<>(), null));
		createOutpatientDocumentService.execute(outpatient, false);

        return ResponseEntity.ok().body(true);
    }

    @Transactional
    @PostMapping("/markProblemAsError")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
    public ResponseEntity<Boolean> markAsErrorHealthCondition(
            @PathVariable Integer institutionId,
            @PathVariable Integer patientId,
            @RequestBody @Valid ErrorProblemDto errorProblemDto) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, ErrorProblemDto {}", institutionId, patientId, errorProblemDto);
        ProblemErrorBo problem = outpatientConsultationMapper.fromErrorProblemDto(errorProblemDto);
        Boolean result = markAsErrorAProblem.run(institutionId, patientId, problem);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/validateProblemAsError/{healthConditionId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
    public ResponseEntity<ProblemInfoDto> validateMarkAsError(
            @PathVariable Integer institutionId,
            @PathVariable Integer patientId,
            @PathVariable Integer healthConditionId) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, healthConditionId {}", institutionId, patientId, healthConditionId);
        ProblemErrorBo problemInfo = canBeMarkAsError.run(institutionId, patientId, healthConditionId);
        ProblemInfoDto result = outpatientConsultationMapper.fromProblemErrorBo(problemInfo);
        return ResponseEntity.ok().body(result);
    }

	private List<CompleteReferenceDto> mapToCompleteReferenceDto(List<ReferenceDto> references, Integer institutionId,
											 Integer doctorId, Integer patientMedicalCoverageId,
											 Integer patientId, Integer encounterId) {
		return references.stream().map(r -> {
				CompleteReferenceDto result = new CompleteReferenceDto();
				result.setNote(r.getNote());
				result.setConsultation(r.getConsultation());
				result.setCareLineId(r.getCareLineId());
				result.setClinicalSpecialtyIds(r.getClinicalSpecialtyIds());
				result.setProblems(r.getProblems());
				result.setFileIds(r.getFileIds());
				result.setDestinationInstitutionId(r.getDestinationInstitutionId());
				result.setPhonePrefix(r.getPhonePrefix());
				result.setPhoneNumber(r.getPhoneNumber());
				result.setPriority(r.getPriority());
				result.setStudy(r.getStudy());
				result.setInstitutionId(institutionId);
				result.setDoctorId(doctorId);
				result.setPatientMedicalCoverageId(patientMedicalCoverageId);
				result.setPatientId(patientId);
				result.setEncounterId(encounterId);
				result.setSourceTypeId((int)SourceType.OUTPATIENT);
				return result;
		}).collect(Collectors.toList());
	}
    
}