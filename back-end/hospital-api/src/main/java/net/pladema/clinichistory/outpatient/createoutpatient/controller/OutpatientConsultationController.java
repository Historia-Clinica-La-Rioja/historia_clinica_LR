package net.pladema.clinichistory.outpatient.createoutpatient.controller;

import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.clinichistory.documents.events.OnGenerateOutpatientDocumentEvent;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.controller.dto.HealthConditionNewConsultationDto;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.EDocumentType;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.ImmunizationBo;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientEvolutionSummaryDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientUpdateImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper.OutpatientConsultationMapper;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientConsultationService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientDocumentService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.OutpatientSummaryService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.ReasonService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientEvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.medicalconsultation.appointment.controller.service.AppointmentExternalService;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.sgx.pdf.PDFDocumentException;
import net.pladema.sgx.dates.configuration.DateTimeProvider;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalServiceImpl;
import net.pladema.staff.service.ClinicalSpecialtyService;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/outpatient/consultations")
public class OutpatientConsultationController implements OutpatientConsultationAPI {

    private static final Logger LOG = LoggerFactory.getLogger(OutpatientConsultationController.class);
    public static final String OUTPUT = "Output -> {}";

    private final CreateOutpatientConsultationService createOutpatientConsultationService;

    private final CreateOutpatientDocumentService createOutpatientDocumentService;

    private final ReasonService reasonService;

    private final ClinicalSpecialtyService clinicalSpecialtyService;

    private final HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService;

    private final OutpatientConsultationMapper outpatientConsultationMapper;

    private final AppointmentExternalService appointmentExternalService;

    private final DateTimeProvider dateTimeProvider;

    private final CreateDocumentFile createDocumentFile;

    private final PatientExternalService patientExternalService;

    @Value("${test.stress.disable.validation:false}")
    private boolean disableValidation;
    
    private final OutpatientSummaryService outpatientSummaryService;

    public OutpatientConsultationController(CreateOutpatientConsultationService createOutpatientConsultationService,
                                            CreateOutpatientDocumentService createOutpatientDocumentService,
                                            ReasonService reasonService,
                                            HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService,
                                            OutpatientConsultationMapper outpatientConsultationMapper,
                                            AppointmentExternalService appointmentExternalService,
                                            DateTimeProvider dateTimeProvider,
                                            CreateDocumentFile createDocumentFile,
                                            OutpatientSummaryService outpatientSummaryService,
                                            ClinicalSpecialtyService clinicalSpecialtyService,
                                            PatientExternalService patientExternalService) {
        this.createOutpatientConsultationService = createOutpatientConsultationService;
        this.createOutpatientDocumentService = createOutpatientDocumentService;
        this.reasonService = reasonService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.outpatientConsultationMapper = outpatientConsultationMapper;
        this.appointmentExternalService = appointmentExternalService;
        this.dateTimeProvider = dateTimeProvider;
        this.createDocumentFile = createDocumentFile;
        this.outpatientSummaryService = outpatientSummaryService;
        this.clinicalSpecialtyService = clinicalSpecialtyService;
        this.patientExternalService = patientExternalService;
    }

    @Override
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Boolean> createOutpatientConsultation(
            Integer institutionId,
            Integer patientId,
            CreateOutpatientDto createOutpatientDto) throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, createOutpatientDto {}", institutionId, patientId, createOutpatientDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, true,
                createOutpatientDto.getClinicalSpecialtyId());

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        List<ReasonBo> reasons = outpatientConsultationMapper.fromListReasonDto(createOutpatientDto.getReasons());
        reasons = reasonService.addReasons(newOutPatient.getId(), reasons);

        OutpatientDocumentBo outpatient = outpatientConsultationMapper.fromCreateOutpatientDto(createOutpatientDto);
        outpatient = createOutpatientDocumentService.create(newOutPatient.getId(), patientInfo, outpatient);

        outpatient.setClinicalSpecialty(clinicalSpecialtyService.getClinicalSpecialty(createOutpatientDto.getClinicalSpecialtyId())
                .orElse(null));

        if (!disableValidation && appointmentExternalService.hasConfirmedAppointment(patientId,doctorId,dateTimeProvider.nowDate()))
            appointmentExternalService.serveAppointment(patientId, doctorId, dateTimeProvider.nowDate());
        outpatient.setReasons(reasons);
        generateDocument(outpatient, institutionId, newOutPatient.getId(), patientId);

        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
    }


    private void generateDocument(OutpatientDocumentBo outpatient, Integer institutionId, Integer outpatientId,
                                  Integer patientId) throws IOException, PDFDocumentException {
        OnGenerateDocumentEvent event = new OnGenerateOutpatientDocumentEvent(outpatient, institutionId, outpatientId,
                EDocumentType.map(DocumentType.OUTPATIENT), patientId);
        createDocumentFile.execute(event);
    }


    @Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO')")
    public ResponseEntity<Boolean> gettingVaccine(
            Integer institutionId,
            Integer patientId,
            List<OutpatientImmunizationDto> vaccineDto) throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, OutpatientImmunizationDto {}", institutionId, patientId, vaccineDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        Integer clinicalSpecialtyId = getClinicalSpecialtyId(vaccineDto);
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, true, clinicalSpecialtyId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setEvolutionNote(extractNotes(vaccineDto));
        outpatient.setImmunizations(extractImmunizations(vaccineDto,institutionId));

        outpatient = createOutpatientDocumentService.create(newOutPatient.getId(), patientInfo, outpatient);
        outpatient.setClinicalSpecialty(clinicalSpecialtyService.getClinicalSpecialty(clinicalSpecialtyId)
                .orElse(null));

        if (!disableValidation && appointmentExternalService.hasConfirmedAppointment(patientId,doctorId,dateTimeProvider.nowDate()))
            appointmentExternalService.serveAppointment(patientId, doctorId, dateTimeProvider.nowDate());
        generateDocument(outpatient, institutionId, newOutPatient.getId(), patientId);

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
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
    public ResponseEntity<Boolean> updateImmunization(
            Integer institutionId,
            Integer patientId,
            OutpatientUpdateImmunizationDto outpatientUpdateImmunization) throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, OutpatientImmunizationDto {}", institutionId, patientId, outpatientUpdateImmunization);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, false, null);

        ImmunizationBo immunizationBo = outpatientConsultationMapper.fromOutpatientImmunizationDto(outpatientUpdateImmunization);
        immunizationBo.setInstitutionId(institutionId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setImmunizations(Collections.singletonList(immunizationBo));

        outpatient = createOutpatientDocumentService.create(newOutPatient.getId(), patientInfo, outpatient);
        generateDocument(outpatient, institutionId, newOutPatient.getId(), patientId);

        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Boolean> solveHealthCondition(
            Integer institutionId,
            Integer patientId,
            @Valid HealthConditionNewConsultationDto solvedProblemDto) throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, HealthConditionNewConsultationDto {}", institutionId, patientId, solvedProblemDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, false, null);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setProblems(Collections.singletonList(outpatientConsultationMapper.fromHealthConditionNewConsultationDto(solvedProblemDto)));
        outpatient = createOutpatientDocumentService.create(newOutPatient.getId(), patientInfo, outpatient);
        generateDocument(outpatient, institutionId, newOutPatient.getId(), patientId);

        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/summary-list")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
    public ResponseEntity<List<OutpatientEvolutionSummaryDto>> getEvolutionSummaryList(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId){
        List<OutpatientEvolutionSummaryBo> evolutions = outpatientSummaryService.getSummary(patientId);
        List<OutpatientEvolutionSummaryDto> result = outpatientConsultationMapper.fromListOutpatientEvolutionSummaryBo(evolutions);
        LOG.debug("Get  summary  => {}", result);
        return ResponseEntity.ok(result);
    }
}