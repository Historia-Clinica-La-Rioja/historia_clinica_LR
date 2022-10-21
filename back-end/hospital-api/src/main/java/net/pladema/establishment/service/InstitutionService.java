package net.pladema.establishment.service;

import net.pladema.address.controller.service.domain.AddressBo;
import net.pladema.establishment.service.domain.InstitutionBo;

import java.util.Optional;

public interface InstitutionService {

    InstitutionBo get(Integer id);
	InstitutionBo get(String sisaCode);
	AddressBo getAddress(Integer institutionId);
}
