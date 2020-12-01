package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;

public interface SnomedService {

    String createSnomedTerm(SnomedBo snomedTerm);

    SnomedBo getSnomed(String id);
}
