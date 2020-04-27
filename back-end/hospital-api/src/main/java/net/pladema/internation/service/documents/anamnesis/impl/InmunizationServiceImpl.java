package net.pladema.internation.service.documents.anamnesis.impl;

import net.pladema.internation.repository.ips.InmunizationRepository;
import net.pladema.internation.repository.ips.entity.Inmunization;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.InmunizationService;
import net.pladema.internation.service.domain.ips.InmunizationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InmunizationServiceImpl implements InmunizationService {

    private static final Logger LOG = LoggerFactory.getLogger(InmunizationServiceImpl.class);

    private final InmunizationRepository inmunizationRepository;

    private final SnomedService snomedService;

    private final DocumentService documentService;

    public InmunizationServiceImpl(InmunizationRepository inmunizationRepository,
                                   SnomedService snomedService,
                                   DocumentService documentService){
        this.inmunizationRepository = inmunizationRepository;
        this.snomedService = snomedService;
        this.documentService = documentService;
    }

    @Override
    public void loadInmunization(Integer patientId, Long documentId, List<InmunizationBo> inmunizations) {
        LOG.debug("Input parameters -> {}", inmunizations);
        inmunizations.forEach(inmunizationBo -> {
            Inmunization inmunization = buildInmunization(patientId, inmunizationBo);
            inmunization = inmunizationRepository.save(inmunization);
            documentService.createInmunization(documentId, inmunization.getId());
        });
    }

    private <T extends InmunizationBo> Inmunization buildInmunization(Integer patientId, T info) {
        LOG.debug("Input parameters -> patientId {}, info {}", patientId, info);
        snomedService.createSnomedTerm(info.getSnomed());
        Inmunization inmunization = new Inmunization(patientId, info.getSnomed().getId(), info.getStatusId()
                , info.getAdministrationDate(), info.isDeleted());
        LOG.debug("Output -> {}", inmunization);
        return inmunization;
    }
}
