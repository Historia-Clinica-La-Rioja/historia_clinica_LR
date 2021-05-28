package net.pladema.clinichistory.documents.core.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.patient.service.StringHelper;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SnomedServiceImpl implements SnomedService {

    public static final String OUTPUT = "Output -> {}";
    private static final Logger LOG = LoggerFactory.getLogger(SnomedServiceImpl.class);

    private final SnomedRepository snomedRepository;

    public SnomedServiceImpl(SnomedRepository snomedRepository){
        this.snomedRepository = snomedRepository;
    }

    @Override
    public Integer createSnomedTerm(SnomedBo snomedTerm){

        LOG.debug("Input parameters -> {}", snomedTerm);
        Snomed snomed = new Snomed();
        if(StringHelper.isNullOrWhiteSpace(snomedTerm.getSctid()) || StringHelper.isNullOrWhiteSpace(snomedTerm.getPt())) {
            LOG.debug(OUTPUT, snomed.getId());
            return snomed.getId();
        }

        String parentId = snomedTerm.getParentId() == null ? snomedTerm.getSctid() : snomedTerm.getParentId();
        String parentFsn = snomedTerm.getParentFsn() == null ? snomedTerm.getPt() : snomedTerm.getParentFsn();

        snomed = new Snomed(
                snomedTerm.getSctid(), snomedTerm.getPt(), parentId, parentFsn);
        snomed = snomedRepository.save(snomed);
        LOG.debug(OUTPUT, snomed.getId());
        if(snomed.getId() == null)
            throw new IllegalArgumentException("snomed.invalid");
        return snomed.getId();
    }

    @Override
    public SnomedBo getSnomed(Integer id){
        return new SnomedBo(snomedRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Snomed-not-found", "Snomed not found")));
    }

    @Override
    public Optional<Integer> getSnomedId(SnomedBo snomedTerm) {
        return snomedRepository.findIdBySctidAndPt(snomedTerm.getSctid(), snomedTerm.getPt());
    }

    @Override
    public Optional<Integer> getLatestIdBySctid(String sctidCode) {
        return snomedRepository.findLatestIdBySctid(sctidCode);
    }
}
