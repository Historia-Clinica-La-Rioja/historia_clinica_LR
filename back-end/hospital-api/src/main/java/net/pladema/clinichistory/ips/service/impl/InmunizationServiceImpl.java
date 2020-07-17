package net.pladema.clinichistory.ips.service.impl;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.ips.repository.InmunizationRepository;
import net.pladema.clinichistory.ips.repository.entity.Inmunization;
import net.pladema.clinichistory.ips.repository.masterdata.InmunizationStatusRepository;
import net.pladema.clinichistory.ips.service.InmunizationService;
import net.pladema.clinichistory.ips.service.SnomedService;
import net.pladema.clinichistory.ips.service.domain.InmunizationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InmunizationServiceImpl implements InmunizationService {

    private static final Logger LOG = LoggerFactory.getLogger(InmunizationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final InmunizationRepository inmunizationRepository;

    private final InmunizationStatusRepository inmunizationStatusRepository;

    private final SnomedService snomedService;

    private final DocumentService documentService;

    public InmunizationServiceImpl(InmunizationRepository inmunizationRepository,
                                   InmunizationStatusRepository inmunizationStatusRepository,
                                   SnomedService snomedService,
                                   DocumentService documentService){
        this.inmunizationRepository = inmunizationRepository;
        this.inmunizationStatusRepository = inmunizationStatusRepository;
        this.snomedService = snomedService;
        this.documentService = documentService;
    }

    @Override
    public List<InmunizationBo> loadInmunization(Integer patientId, Long documentId, List<InmunizationBo> immunizations) {
        LOG.debug("Input parameters -> patientId {}, documentId {}, immunizations {}", patientId, documentId, immunizations);
        immunizations.stream().forEach(i -> {
            String sctId = snomedService.createSnomedTerm(i.getSnomed());
            Inmunization inmunization = saveInmunization(patientId, i, sctId);

            i.setId(inmunization.getId());
            i.setStatusId(inmunization.getStatusId());
            i.setStatus(getStatus(i.getStatusId()));
            i.setAdministrationDate(inmunization.getAdministrationDate());

            documentService.createInmunization(documentId, inmunization.getId());
        });
        List<InmunizationBo> result = immunizations;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private Inmunization saveInmunization(Integer patientId, InmunizationBo inmunizationBo, String sctId) {
        LOG.debug("Input parameters -> patientId {}, inmunizationBo {}, sctId {}", patientId, inmunizationBo, sctId);
        Inmunization inmunization = new Inmunization(patientId, sctId, inmunizationBo.getStatusId()
                , inmunizationBo.getAdministrationDate());
        inmunization = inmunizationRepository.save(inmunization);
        LOG.debug("Inmunization saved -> {}", inmunization.getId());
        LOG.debug(OUTPUT, inmunization);
        return inmunization;
    }

    private String getStatus(String id) {
        return inmunizationStatusRepository.findById(id).get().getDescription();
    }

}
