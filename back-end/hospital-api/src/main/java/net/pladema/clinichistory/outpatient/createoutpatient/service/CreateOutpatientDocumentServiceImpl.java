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
    public OutpatientDocumentBo execute(Integer institutionId, OutpatientDocumentBo outpatient) throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> institutionId {}, outpatient {}", institutionId, outpatient);

        outpatient.setId(documentFactory.run(outpatient));

        updateOutpatientConsultationService.updateOutpatientDocId(outpatient.getEncounterId(), outpatient.getId());
        LOG.debug(OUTPUT, outpatient);

        generateDocument(outpatient, institutionId);
        return outpatient;
    }

    private void generateDocument(OutpatientDocumentBo outpatient, Integer institutionId) throws IOException, PDFDocumentException {
        OnGenerateInternmentDocumentEvent event = new OnGenerateInternmentDocumentEvent(outpatient, institutionId, outpatient.getEncounterId(),
                EDocumentType.map(DocumentType.OUTPATIENT), outpatient.getPatientId());
        createDocumentFile.execute(event);
    }
}

