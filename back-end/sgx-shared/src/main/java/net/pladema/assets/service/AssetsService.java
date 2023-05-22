package net.pladema.assets.service;

import java.util.Optional;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import net.pladema.assets.service.domain.Assets;

public interface AssetsService {

    Optional<Assets> findByName(String name);

	StoredFileBo getFile(String fileName);
}
