package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpdateOutpatientDocumentServiceImpl implements UpdateOutpatientConsultationService {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateOutpatientDocumentServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final OutpatientConsultationRepository outpatientConsultationRepository;

    public UpdateOutpatientDocumentServiceImpl(OutpatientConsultationRepository outpatientConsultationRepository) {
        this.outpatientConsultationRepository = outpatientConsultationRepository;
    }


    @Override
    public boolean updateOutpatientDocId(Integer outpatientId, Long docId) {
        LOG.debug("Input parameters outpatientId {}, docId {}", outpatientId, docId);
        outpatientConsultationRepository.findById(outpatientId).ifPresent(ot -> {
                ot.setDocumentId(docId);
                outpatientConsultationRepository.save(ot);
        });
        LOG.debug(OUTPUT, true);
        return true;
    }
}

