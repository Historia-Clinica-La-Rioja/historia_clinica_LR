package net.pladema.clinichistory.outpatient.createoutpatient.controller;

import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import com.itextpdf.text.DocumentException;
import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper.OutpatientConsultationMapper;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientConsultationService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.CreateOutpatientDocumentService;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.pdf.service.PdfService;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;

@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/outpatient/consultations")
public class OutpatientConsultationController implements OutpatientConsultationAPI {

    private static final Logger LOG = LoggerFactory.getLogger(OutpatientConsultationController.class);
    public static final String OUTPUT = "Output -> {}";

    private final CreateOutpatientConsultationService createOutpatientConsultationService;

    private final CreateOutpatientDocumentService createOutpatientDocumentService;

    private final HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService;

    private final OutpatientConsultationMapper outpatientConsultationMapper;

    private final PdfService pdfService;

    public OutpatientConsultationController(CreateOutpatientConsultationService createOutpatientConsultationService,
                                            CreateOutpatientDocumentService createOutpatientDocumentService,
                                            HealthcareProfessionalExternalServiceImpl healthcareProfessionalExternalService,
                                            OutpatientConsultationMapper outpatientConsultationMapper,
                                            PdfService pdfService) {
        this.createOutpatientConsultationService = createOutpatientConsultationService;
        this.createOutpatientDocumentService = createOutpatientDocumentService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.outpatientConsultationMapper = outpatientConsultationMapper;
        this.pdfService = pdfService;
    }

    @Override
    @Transactional
    public ResponseEntity<Boolean> createOutpatientConsultation(
            Integer institutionId,
            Integer patientId,
            CreateOutpatientDto createOutpatientDto) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, createOutpatientDto {}", institutionId, patientId, createOutpatientDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        OutpatientBo newOutPatient = createOutpatientConsultationService.create(institutionId, patientId, doctorId, true);
        OutpatientDocumentBo outpatient = outpatientConsultationMapper.fromCreateOutpatientDto(createOutpatientDto);
        outpatient = createOutpatientDocumentService.create(newOutPatient.getId(), patientId, outpatient);
        generateDocument(outpatient, institutionId, newOutPatient.getId(), patientId);

        LOG.debug(OUTPUT, true);
        return  ResponseEntity.ok().body(true);
    }


    private void generateDocument(OutpatientDocumentBo outpatient, Integer institutionId, Integer outpatientId,
                                  Integer patientId) throws IOException, DocumentException {
        OnGenerateDocumentEvent event = new OnGenerateDocumentEvent(outpatient, institutionId, outpatientId,
                DocumentType.OUTPATIENT, "outpatient", patientId);
        pdfService.loadDocument(event);
    }

}