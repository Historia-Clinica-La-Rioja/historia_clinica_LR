package net.pladema.clinichistory.documents.service.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;

import java.util.Optional;

public interface SnomedService {

    Integer createSnomedTerm(SnomedBo snomedTerm);

    SnomedBo getSnomed(Integer id);

    Optional<Integer> getSnomedId(SnomedBo snomedTerm);

    Optional<Integer> getLatestIdBySctid(String sctidCode);
}
