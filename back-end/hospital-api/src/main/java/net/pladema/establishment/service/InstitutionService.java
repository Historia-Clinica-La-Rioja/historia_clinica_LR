package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.InstitutionBo;

public interface InstitutionService {

    InstitutionBo get(Integer id);
	InstitutionBo get(String sisaCode);
}
