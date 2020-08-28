package net.pladema.clinichistory.ips.service.impl;

import net.pladema.clinichistory.ips.repository.masterdata.SnomedRepository;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;
import net.pladema.clinichistory.ips.service.domain.SnomedBo;
import net.pladema.clinichistory.ips.service.SnomedService;
import net.pladema.patient.service.StringHelper;
import net.pladema.sgx.exceptions.NotFoundException;
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
        if(StringHelper.isNullOrWhiteSpace(snomedTerm.getId()) || StringHelper.isNullOrWhiteSpace(snomedTerm.getPt())) {
            LOG.debug(OUTPUT, snomed.getId());
            return snomed.getId();
        }

        String parentId = snomedTerm.getParentId() == null ? snomedTerm.getId() : snomedTerm.getParentId();
        String parentFsn = snomedTerm.getParentFsn() == null ? snomedTerm.getPt() : snomedTerm.getParentFsn();

        snomed = new Snomed(
                snomedTerm.getId(), snomedTerm.getPt(), parentId, parentFsn);
        snomed = snomedRepository.save(snomed);
        LOG.debug(OUTPUT, snomed.getId());
        if(snomed.getId() == null)
            throw new IllegalArgumentException("snomed.invalid");
        return snomed.getId();
    }

    @Override
    public SnomedBo getSnomed(String id){
        return new SnomedBo(snomedRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Snomed-not-found", "Snomed not found")));
    }

}
