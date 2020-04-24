package net.pladema.internation.service.impl;

import net.pladema.internation.controller.dto.SnomedDto;
import net.pladema.internation.repository.masterdata.SnomedRepository;
import net.pladema.internation.repository.masterdata.entity.Snomed;
import net.pladema.internation.service.SnomedService;
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
    public String createSnomedTerm(SnomedDto snomedTerm){
        LOG.debug("Input parameters -> {}", snomedTerm);
        Snomed snomed = new Snomed();
        if(StringHelper.isNullOrWhiteSpace(snomedTerm.getId()) || StringHelper.isNullOrWhiteSpace(snomedTerm.getFsn())) {
            LOG.debug(OUTPUT, snomed.getId());
            return snomed.getId();
        }
        snomed = new Snomed(
                snomedTerm.getId(),
                snomedTerm.getFsn(),
                snomedTerm.getId(),
                snomedTerm.getFsn());
        snomed = snomedRepository.save(snomed);
        LOG.debug(OUTPUT, snomed.getId());
        return snomed.getId();
    }

}
