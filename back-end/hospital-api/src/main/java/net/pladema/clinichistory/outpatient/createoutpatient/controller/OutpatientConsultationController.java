package net.pladema.clinichistory.outpatient.createoutpatient.controller;

import com.itextpdf.text.DocumentException;
import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.clinichistory.documents.events.OnGenerateOutpatientDocumentEvent;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.ips.repository.masterdata.entity.EDocumentType;
import net.pladema.clinichistory.ips.service.domain.ImmunizationBo;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientUpdateImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper.OutpatientConsultationMapper;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientConsultationService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientDocumentService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.ReasonService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.pdf.service.PdfService;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/outpatient/consultations")
public class OutpatientConsultationController implements OutpatientConsultationAPI {

    private static final Logger LOG = LoggerFactory.getLogger(OutpatientConsultationController.class);
    public static final String OUTPUT = "Output -> {}";

    private final CreateOutpatientConsultationService createOutpatientConsultationService;

    private final CreateOutpatientDocumentService createOutpatientDocumentService;

    private final ReasonService reasonService;

    private final HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService;

    private final OutpatientConsultationMapper outpatientConsultationMapper;

    private final PdfService pdfService;

    public OutpatientConsultationController(CreateOutpatientConsultationService createOutpatientConsultationService,
                                            CreateOutpatientDocumentService createOutpatientDocumentService,
                                            ReasonService reasonService,
                                            HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService,
                                            OutpatientConsultationMapper outpatientConsultationMapper,
                                            PdfService pdfService) {
        this.createOutpatientConsultationService = createOutpatientConsultationService;
        this.createOutpatientDocumentService = createOutpatientDocumentService;
        this.reasonService = reasonService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.outpatientConsultationMapper = outpatientConsultationMapper;
        this.pdfService = pdfService;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Boolean> createOutpatientConsultation(
            Integer institutionId,
            Integer patientId,
            CreateOutpatientDto createOutpatientDto) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, createOutpatientDto {}", institutionId, patientId, createOutpatientDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, true);

        List<ReasonBo> reasons = outpatientConsultationMapper.fromListReasonDto(createOutpatientDto.getReasons());
        reasons = reasonService.addReasons(newOutPatient.getId(), reasons);

        OutpatientDocumentBo outpatient = outpatientConsultationMapper.fromCreateOutpatientDto(createOutpatientDto);
        outpatient = createOutpatientDocumentService.create(newOutPatient.getId(), patientId, outpatient);

        outpatient.setReasons(reasons);
        generateDocument(outpatient, institutionId, newOutPatient.getId(), patientId);

        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
    }


    private void generateDocument(OutpatientDocumentBo outpatient, Integer institutionId, Integer outpatientId,
                                  Integer patientId) throws IOException, DocumentException {
        OnGenerateDocumentEvent event = new OnGenerateOutpatientDocumentEvent(outpatient, institutionId, outpatientId,
                EDocumentType.map(DocumentType.OUTPATIENT), patientId);
        pdfService.loadDocument(event);
    }


    @Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO')")
    public ResponseEntity<Boolean> gettingVaccine(
            Integer institutionId,
            Integer patientId,
            OutpatientImmunizationDto vaccineDto) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, OutpatientImmunizationDto {}", institutionId, patientId, vaccineDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, true);

        ImmunizationBo immunizationBo = outpatientConsultationMapper.fromOutpatientImmunizationDto(vaccineDto);
        immunizationBo.setInstitutionId(institutionId);

        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setEvolutionNote(vaccineDto.getNote());
        outpatient.setImmunizations(Arrays.asList(immunizationBo));

        outpatient = createOutpatientDocumentService.create(newOutPatient.getId(), patientId, outpatient);
        generateDocument(outpatient, institutionId, newOutPatient.getId(), patientId);

        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
    public ResponseEntity<Boolean> updateImmunization(
            Integer institutionId,
            Integer patientId,
            OutpatientUpdateImmunizationDto outpatientUpdateImmunization) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, OutpatientImmunizationDto {}", institutionId, patientId, outpatientUpdateImmunization);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, false);

        ImmunizationBo immunizationBo = outpatientConsultationMapper.fromOutpatientImmunizationDto(outpatientUpdateImmunization);
        immunizationBo.setInstitutionId(institutionId);

        OutpatientDocumentBo outpatient = new OutpatientDocumentBo();
        outpatient.setImmunizations(Arrays.asList(immunizationBo));

        outpatient = createOutpatientDocumentService.create(newOutPatient.getId(), patientId, outpatient);
        generateDocument(outpatient, institutionId, newOutPatient.getId(), patientId);

        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
    }
}