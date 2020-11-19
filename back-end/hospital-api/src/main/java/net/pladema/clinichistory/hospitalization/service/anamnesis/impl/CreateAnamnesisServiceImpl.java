package net.pladema.clinichistory.hospitalization.service.anamnesis.impl;

import net.pladema.clinichistory.documents.events.OnGenerateInternmentDocumentEvent;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.EDocumentType;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.constraints.AnamnesisValid;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.CreateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.sgx.pdf.PDFDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

import static net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.AnamnesisController.INVALID_EPISODE;

@Service
public class CreateAnamnesisServiceImpl implements CreateAnamnesisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnamnesisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateDocumentFile createDocumentFile;

    public CreateAnamnesisServiceImpl(DocumentFactory documentFactory,
                                      InternmentEpisodeService internmentEpisodeService,
                                      CreateDocumentFile createDocumentFile) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.createDocumentFile = createDocumentFile;
    }

    @Override
    @Transactional
    @AnamnesisValid
    public AnamnesisBo createDocument(Integer institutionId, AnamnesisBo anamnesis)
            throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> anamnesis {}", anamnesis);
        Integer patientId = internmentEpisodeService.getPatient(anamnesis.getEncounterId())
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        anamnesis.setPatientId(patientId);

        documentFactory.run(anamnesis);
        LOG.debug(OUTPUT, anamnesis);
        generateDocument(anamnesis, institutionId);

        return anamnesis;
    }

    private void generateDocument(AnamnesisBo anamnesis, Integer institutionId) throws IOException, PDFDocumentException {
        OnGenerateInternmentDocumentEvent event = new OnGenerateInternmentDocumentEvent(anamnesis, institutionId, anamnesis.getEncounterId(),
                EDocumentType.map(DocumentType.ANAMNESIS), anamnesis.getPatientId());
        createDocumentFile.execute(event);
    }

}
