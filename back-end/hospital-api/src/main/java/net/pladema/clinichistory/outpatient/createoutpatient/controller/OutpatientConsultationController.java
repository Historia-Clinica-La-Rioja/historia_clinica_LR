package net.pladema.clinichistory.outpatient.createoutpatient.controller;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionNewConsultationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.DocumentAppointmentDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientEvolutionSummaryDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientUpdateImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper.OutpatientConsultationMapper;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientConsultationService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientDocumentService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.OutpatientSummaryService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.EvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason.OutpatientReasonService;
import net.pladema.medicalconsultation.appointment.controller.service.AppointmentExternalService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalServiceImpl;

@RestController
@Validated
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
    
    private final OutpatientSummaryService outpatientSummaryService;

	private final SharedAppointmentPort sharedAppointmentPort;

    private final SharedReferenceCounterReference sharedReferenceCounterReference;

    public OutpatientConsultationController(CreateOutpatientConsultationService createOutpatientConsultationService,
                                            CreateOutpatientDocumentService createOutpatientDocumentService,
                                            OutpatientReasonService outpatientReasonService,
                                            HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService,
                                            OutpatientConsultationMapper outpatientConsultationMapper,
                                            AppointmentExternalService appointmentExternalService,
                                            DateTimeProvider dateTimeProvider,
                                            OutpatientSummaryService outpatientSummaryService,
                                            PatientExternalService patientExternalService,
											SharedAppointmentPort sharedAppointmentPort,
                                            SharedReferenceCounterReference sharedReferenceCounterReference) {
        this.createOutpatientConsultationService = createOutpatientConsultationService;
        this.createOutpatientDocumentService = createOutpatientDocumentService;
        this.outpatientReasonService = outpatientReasonService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.outpatientConsultationMapper = outpatientConsultationMapper;
        this.appointmentExternalService = appointmentExternalService;
        this.dateTimeProvider = dateTimeProvider;
        this.outpatientSummaryService = outpatientSummaryService;
        this.patientExternalService = patientExternalService;
		this.sharedAppointmentPort = sharedAppointmentPort;
        this.sharedReferenceCounterReference = sharedReferenceCounterReference;
    }

    @Override
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Boolean> createOutpatientConsultation(
            Integer institutionId,
            Integer patientId,
            CreateOutpatientDto createOutpatientDto) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, createOutpatientDto {}", institutionId, patientId, createOutpatientDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());

        //Find the associated appointments and get the OS
        Integer patientMedicalCoverageId = appointmentExternalService.getMedicalCoverage(patientId, doctorId);

        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, true,
                createOutpatientDto.getClinicalSpecialtyId(), patientMedicalCoverageId);

        OutpatientDocumentBo outpatient = outpatientConsultationMapper.fromCreateOutpatientDto(createOutpatientDto);
        outpatient.setEncounterId(newOutPatient.getId());
        outpatient.setInstitutionId(institutionId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        outpatient.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));
        outpatient.setPatientId(patientId);

        List<ReasonBo> reasons = outpatientConsultationMapper.fromListReasonDto(createOutpatientDto.getReasons());
        outpatient.setReasons(reasons);
        outpatientReasonService.addReasons(newOutPatient.getId(), reasons);

        Long documentId = createOutpatientDocumentService.execute(outpatient).getId();
		Integer appointmentId = null;
        if (!disableValidation && (appointmentExternalService.hasCurrentAppointment(patientId,doctorId,dateTimeProvider.nowDate())
				|| appointmentExternalService.hasOldAppointment(patientId,doctorId))) {
			appointmentId = appointmentExternalService.serveAppointment(patientId, doctorId, dateTimeProvider.nowDate());
		}
		if(appointmentId != null)
			this.sharedAppointmentPort.saveDocumentAppointment(new DocumentAppointmentDto(documentId, appointmentId));

        if(!createOutpatientDto.getReferences().isEmpty()) {
            sharedReferenceCounterReference.saveReferences(newOutPatient.getId(), (int)SourceType.OUTPATIENT, createOutpatientDto.getReferences());
        }

        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
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

        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, true, clinicalSpecialtyId, null);
        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setEncounterId(newOutPatient.getId());
        outpatient.setInstitutionId(institutionId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        outpatient.setPatientId(patientId);
        outpatient.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));

        outpatient.setEvolutionNote(extractNotes(vaccineDto));
        outpatient.setImmunizations(extractImmunizations(vaccineDto,institutionId));

        outpatient.setClinicalSpecialtyId(clinicalSpecialtyId);

        createOutpatientDocumentService.execute(outpatient);


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
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, false, null, null);

        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setEncounterId(newOutPatient.getId());
        outpatient.setInstitutionId(institutionId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        outpatient.setPatientId(patientId);
        outpatient.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));

        ImmunizationBo immunizationBo = outpatientConsultationMapper.fromOutpatientImmunizationDto(outpatientUpdateImmunization);
        immunizationBo.setInstitutionId(institutionId);
        outpatient.setImmunizations(Collections.singletonList(immunizationBo));

        createOutpatientDocumentService.execute(outpatient);

        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Boolean> solveHealthCondition(
            Integer institutionId,
            Integer patientId,
            @Valid HealthConditionNewConsultationDto solvedProblemDto) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, HealthConditionNewConsultationDto {}", institutionId, patientId, solvedProblemDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, false, null, null);
        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setEncounterId(newOutPatient.getId());
        outpatient.setInstitutionId(institutionId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        outpatient.setPatientId(patientId);
        outpatient.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));

        outpatient.setProblems(Collections.singletonList(outpatientConsultationMapper.fromHealthConditionNewConsultationDto(solvedProblemDto)));
        outpatient.getProblems().forEach(p->p.setId(null));
		createOutpatientDocumentService.execute(outpatient);

        return ResponseEntity.ok().body(true);
    }


    @GetMapping("/summary-list")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<List<OutpatientEvolutionSummaryDto>> getEvolutionSummaryList(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId){
        List<EvolutionSummaryBo> evolutions = outpatientSummaryService.getSummary(patientId);
        List<OutpatientEvolutionSummaryDto> result = outpatientConsultationMapper.fromListOutpatientEvolutionSummaryBo(evolutions);
        LOG.debug("Get summary  => {}", result);
        return ResponseEntity.ok(result);
    }
}