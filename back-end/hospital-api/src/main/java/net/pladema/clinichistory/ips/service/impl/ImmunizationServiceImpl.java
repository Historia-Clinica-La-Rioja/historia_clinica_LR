package net.pladema.clinichistory.ips.service.impl;

import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.ips.repository.InmunizationRepository;
import net.pladema.clinichistory.ips.repository.entity.Inmunization;
import net.pladema.clinichistory.ips.repository.masterdata.InmunizationStatusRepository;
import net.pladema.clinichistory.ips.repository.masterdata.entity.InmunizationStatus;
import net.pladema.clinichistory.ips.service.ImmunizationService;
import net.pladema.clinichistory.ips.service.SnomedService;
import net.pladema.clinichistory.ips.service.domain.InmunizationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImmunizationServiceImpl implements ImmunizationService {

    private static final Logger LOG = LoggerFactory.getLogger(ImmunizationServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final InmunizationRepository inmunizationRepository;

    private final InmunizationStatusRepository inmunizationStatusRepository;

    private final SnomedService snomedService;

    private final DocumentService documentService;

    public ImmunizationServiceImpl(InmunizationRepository inmunizationRepository,
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
        immunizations.forEach(i -> {
            String sctId = snomedService.createSnomedTerm(i.getSnomed());
            Inmunization immunization = saveImmunization(patientId, i, sctId);

            i.setId(immunization.getId());
            i.setStatusId(immunization.getStatusId());
            i.setStatus(getStatus(i.getStatusId()));
            i.setAdministrationDate(immunization.getAdministrationDate());

            documentService.createImmunization(documentId, immunization.getId());
        });
        List<InmunizationBo> result = immunizations;
        LOG.debug(OUTPUT, result);
        return result;
    }

    private Inmunization saveImmunization(Integer patientId, InmunizationBo immunizationBo, String sctId) {
        LOG.debug("Input parameters -> patientId {}, immunizationBo {}, sctId {}", patientId, immunizationBo, sctId);
        Inmunization immunization = new Inmunization(patientId, sctId, immunizationBo.getStatusId()
                , immunizationBo.getAdministrationDate());
        immunization = inmunizationRepository.save(immunization);
        LOG.debug("Immunization saved -> {}", immunization.getId());
        LOG.debug(OUTPUT, immunization);
        return immunization;
    }

    private String getStatus(String id) {
        return inmunizationStatusRepository.findById(id).map(InmunizationStatus::getDescription).orElse(null);
    }

}
