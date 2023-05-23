package net.pladema.terminology.cache.infrastructure.output;

import net.pladema.snowstorm.services.loadCsv.UpdateConceptsResultBo;

public interface SnomedCacheFileIngestor {
	UpdateConceptsResultBo run(
			Long fileId,
			String eclKey
	);
}
