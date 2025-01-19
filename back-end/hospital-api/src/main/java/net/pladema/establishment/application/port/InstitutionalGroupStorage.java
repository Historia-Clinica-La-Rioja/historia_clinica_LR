package net.pladema.establishment.application.port;

import net.pladema.establishment.service.domain.InstitutionalGroupBo;

import java.util.List;

public interface InstitutionalGroupStorage {

	List<InstitutionalGroupBo> getInstitutionalGroupsByUserId(Integer userId);
	
}
