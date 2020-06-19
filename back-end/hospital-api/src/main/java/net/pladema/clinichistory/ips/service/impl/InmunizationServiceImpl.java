package net.pladema.clinichistory.ips.service.impl;

import net.pladema.clinichistory.ips.repository.InmunizationRepository;
import net.pladema.clinichistory.ips.repository.entity.Inmunization;
import net.pladema.clinichistory.ips.repository.generalstate.InmunizationVo;
import net.pladema.clinichistory.ips.service.domain.InmunizationBo;
import net.pladema.clinichistory.ips.service.SnomedService;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.ips.service.InmunizationService;
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
        inmunizations.stream().forEach(inmunizationBo -> {
            String sctId = snomedService.createSnomedTerm(inmunizationBo.getSnomed());
            Inmunization inmunization = saveInmunization(patientId, inmunizationBo, sctId);

            inmunizationBo.setId(inmunization.getId());
            inmunizationBo.setStatusId(inmunization.getStatusId());
            inmunizationBo.setAdministrationDate(inmunization.getAdministrationDate());

            documentService.createInmunization(documentId, inmunization.getId());
        });
        List<InmunizationBo> result = inmunizations;
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

    @Override
    public List<InmunizationBo> getInmunizationsGeneralState(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
        List<InmunizationVo> queryResult = inmunizationRepository.findGeneralState(internmentEpisodeId);
        List<InmunizationBo> result = queryResult.stream().map(InmunizationBo::new).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }
}
