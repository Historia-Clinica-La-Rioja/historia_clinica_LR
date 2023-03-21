package net.pladema.person.service;

import net.pladema.clinichistory.requests.servicerequests.service.domain.StoredFileBo;

public interface GetPersonFileService {

	StoredFileBo run(Integer fileId);
}
