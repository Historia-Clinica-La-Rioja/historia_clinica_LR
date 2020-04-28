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
import java.util.stream.Collectors;

@Service
public class InmunizationServiceImpl implements InmunizationService {

    private static final Logger LOG = LoggerFactory.getLogger(InmunizationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

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
    public List<InmunizationBo> loadInmunization(Integer patientId, Long documentId, List<InmunizationBo> inmunizations) {
        LOG.debug("Input parameters -> {}", inmunizations);
        inmunizations.stream().filter(inmunizationBo -> inmunizationBo.mustSave()).forEach(inmunizationBo -> {
            Inmunization inmunization = saveInmunization(patientId, inmunizationBo);
            inmunizationBo.setId(inmunization.getId());
            documentService.createInmunization(documentId, inmunization.getId());
        });
        return inmunizations.stream().filter(i -> !i.isDeleted()).collect(Collectors.toList());
    }

    private <T extends InmunizationBo> Inmunization buildInmunization(Integer patientId, T info) {
        LOG.debug("Input parameters -> patientId {}, info {}", patientId, info);
        snomedService.createSnomedTerm(info.getSnomed());
        Inmunization inmunization = new Inmunization(patientId, info.getSnomed().getId(), info.getStatusId()
                , info.getAdministrationDate(), info.isDeleted());
        LOG.debug(OUTPUT, inmunization);
        return inmunization;
    }

    private Inmunization saveInmunization(Integer patientId, InmunizationBo inmunizationBo) {
        LOG.debug("Input parameters -> patientId {}, inmunizationBo {}", patientId, inmunizationBo);
        Inmunization inmunization = buildInmunization(patientId, inmunizationBo);
        inmunization = inmunizationRepository.save(inmunization);
        LOG.debug("Inmunization saved ->", inmunization.getId());
        LOG.debug(OUTPUT, inmunization);
        return inmunization;
    }
}
