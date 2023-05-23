package net.pladema.terminology.cache.infrastructure.output;

import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;

public interface SnomedCacheFileStorage {
	FileInfo save(String url);
}
