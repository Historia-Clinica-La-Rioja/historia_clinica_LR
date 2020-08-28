package net.pladema.clinichistory.ips.service;

import net.pladema.clinichistory.ips.service.domain.SnomedBo;

public interface SnomedService {

    String createSnomedTerm(SnomedBo snomedTerm);

    SnomedBo getSnomed(String id);
}
