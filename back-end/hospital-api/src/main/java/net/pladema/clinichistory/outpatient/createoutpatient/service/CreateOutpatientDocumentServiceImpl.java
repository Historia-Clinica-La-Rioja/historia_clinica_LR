package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Collections;


@Service
public class CreateOutpatientDocumentServiceImpl implements CreateOutpatientDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOutpatientDocumentServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final UpdateOutpatientConsultationService updateOutpatientConsultationService;

    public CreateOutpatientDocumentServiceImpl(DocumentFactory documentFactory,
                                              UpdateOutpatientConsultationService updateOutpatientConsultationService) {
        this.documentFactory = documentFactory;
        this.updateOutpatientConsultationService = updateOutpatientConsultationService;
    }


    @Override
    public OutpatientDocumentBo execute(OutpatientDocumentBo outpatient) {
        LOG.debug("Input parameters -> outpatient {}", outpatient);

        assertContextValid(outpatient);
        outpatient.setId(documentFactory.run(outpatient, true));

        updateOutpatientConsultationService.updateOutpatientDocId(outpatient.getEncounterId(), outpatient.getId());
        LOG.debug(OUTPUT, outpatient);

        return outpatient;
    }

    private void assertContextValid(OutpatientDocumentBo outpatient) {
        if (outpatient.getInstitutionId() == null)
            throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
        if (outpatient.getEncounterId() == null)
            throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());
    }
}

