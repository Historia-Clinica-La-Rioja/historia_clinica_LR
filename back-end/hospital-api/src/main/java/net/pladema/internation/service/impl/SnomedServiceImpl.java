package net.pladema.internation.service.impl;

import net.pladema.internation.repository.masterdata.SnomedRepository;
import net.pladema.internation.repository.masterdata.entity.Snomed;
import net.pladema.internation.service.SnomedService;
import net.pladema.internation.service.domain.SnomedBo;
import net.pladema.patient.service.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SnomedServiceImpl implements SnomedService {

    public static final String OUTPUT = "Output -> {}";
    private static final Logger LOG = LoggerFactory.getLogger(SnomedServiceImpl.class);

    private final SnomedRepository snomedRepository;

    public SnomedServiceImpl(SnomedRepository snomedRepository){
        this.snomedRepository = snomedRepository;
    }

    @Override
    public String createSnomedTerm(SnomedBo snomedTerm){

        LOG.debug("Input parameters -> {}", snomedTerm);
        Snomed snomed = new Snomed();
        if(StringHelper.isNullOrWhiteSpace(snomedTerm.getId()) || StringHelper.isNullOrWhiteSpace(snomedTerm.getFsn())) {
            LOG.debug(OUTPUT, snomed.getId());
            return snomed.getId();
        }

        String parentId = snomedTerm.getParentId() == null ? snomedTerm.getId() : snomedTerm.getParentId();
        String parentFsn = snomedTerm.getParentFsn() == null ? snomedTerm.getFsn() : snomedTerm.getParentFsn();

        snomed = new Snomed(
                snomedTerm.getId(), snomedTerm.getFsn(), parentId, parentFsn);
        snomed = snomedRepository.save(snomed);
        LOG.debug(OUTPUT, snomed.getId());
        if(snomed.getId() == null)
            throw new IllegalArgumentException("snomed.invalid");
        return snomed.getId();
    }

}
