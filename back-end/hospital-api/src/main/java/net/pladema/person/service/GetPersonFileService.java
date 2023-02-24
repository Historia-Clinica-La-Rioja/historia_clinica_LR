package net.pladema.person.service;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;

public interface GetPersonFileService {

	StoredFileBo run(Integer fileId);
}
