package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.documents.events.OnGenerateInternmentDocumentEvent;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.EDocumentType;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.sgx.pdf.PDFDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import javax.validation.ConstraintViolationException;
import java.util.Collections;


@Service
public class CreateOutpatientDocumentServiceImpl implements CreateOutpatientDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOutpatientDocumentServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final CreateDocumentFile createDocumentFile;

    private final UpdateOutpatientConsultationService updateOutpatientConsultationService;

    public CreateOutpatientDocumentServiceImpl(DocumentFactory documentFactory,
                                               CreateDocumentFile createDocumentFile, UpdateOutpatientConsultationService updateOutpatientConsultationService) {
        this.documentFactory = documentFactory;
        this.createDocumentFile = createDocumentFile;
        this.updateOutpatientConsultationService = updateOutpatientConsultationService;
    }


    @Override
    public OutpatientDocumentBo execute(OutpatientDocumentBo outpatient) throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> outpatient {}", outpatient);

        assertContextValid(outpatient);
        outpatient.setId(documentFactory.run(outpatient));

        updateOutpatientConsultationService.updateOutpatientDocId(outpatient.getEncounterId(), outpatient.getId());
        LOG.debug(OUTPUT, outpatient);

        generateDocument(outpatient, outpatient.getInstitutionId());
        return outpatient;
    }

    private void generateDocument(OutpatientDocumentBo outpatient, Integer institutionId) throws IOException, PDFDocumentException {
        OnGenerateInternmentDocumentEvent event = new OnGenerateInternmentDocumentEvent(outpatient, institutionId, outpatient.getEncounterId(),
                EDocumentType.map(DocumentType.OUTPATIENT), outpatient.getPatientId());
        createDocumentFile.execute(event);
    }

    private void assertContextValid(OutpatientDocumentBo outpatient) {
        if (outpatient.getInstitutionId() == null)
            throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
        if (outpatient.getEncounterId() == null)
            throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());
    }
}

