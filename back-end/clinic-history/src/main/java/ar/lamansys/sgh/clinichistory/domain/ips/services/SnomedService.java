package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.strings.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SnomedService {

    public static final String OUTPUT = "Output -> {}";
    private static final Logger LOG = LoggerFactory.getLogger(SnomedService.class);

    private final SnomedRepository snomedRepository;

    public SnomedService(SnomedRepository snomedRepository){
        this.snomedRepository = snomedRepository;
    }

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

    public List<Integer> createSnomedTerms(List<SnomedBo> snomedTerms){
        LOG.debug("Input parameter -> snomedTerms size = {}", snomedTerms.size());
        LOG.trace("Input parameter -> snomedTerms {}", snomedTerms);
        List<Snomed> toSave = snomedTerms.stream()
                .map(this::mapToEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Integer> result = snomedRepository.saveAll(toSave)
                .stream()
                .map(Snomed::getId)
                .collect(Collectors.toList());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    private Snomed mapToEntity(SnomedBo snomedBo) {
        LOG.debug("Input parameter -> snomedBo {}", snomedBo);
        if(StringHelper.isNullOrWhiteSpace(snomedBo.getSctid()) || StringHelper.isNullOrWhiteSpace(snomedBo.getPt())) {
            return null;
        }
        String parentId = snomedBo.getParentId() == null ? snomedBo.getSctid() : snomedBo.getParentId();
        String parentFsn = snomedBo.getParentFsn() == null ? snomedBo.getPt() : snomedBo.getParentFsn();

        Snomed result = new Snomed(snomedBo.getSctid(), snomedBo.getPt(), parentId, parentFsn);
        LOG.debug("Output -> {}", result);
        return result;
    }

    public SnomedBo getSnomed(Integer id){
        return new SnomedBo(snomedRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Snomed-not-found", "Snomed not found")));
    }

    public Optional<Integer> getSnomedId(SnomedBo snomedTerm) {
        return snomedRepository.findIdBySctidAndPt(snomedTerm.getSctid(), snomedTerm.getPt());
    }

    public Optional<Integer> getLatestIdBySctid(String sctidCode) {
        return snomedRepository.findLatestIdBySctid(sctidCode);
    }
}
