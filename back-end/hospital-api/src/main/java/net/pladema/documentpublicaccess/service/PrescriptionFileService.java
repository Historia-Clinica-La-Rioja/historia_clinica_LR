package net.pladema.documentpublicaccess.service;

import java.util.Optional;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;

public interface PrescriptionFileService {
	Optional<StoredFileBo> getFile(String accessId);
}
