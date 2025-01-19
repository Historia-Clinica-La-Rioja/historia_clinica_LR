package net.pladema.terminology.cache.infrastructure.output;

import net.pladema.snowstorm.services.loadCsv.UpdateConceptsResultBo;
import net.pladema.terminology.cache.controller.dto.ETerminologyKind;

public interface SnomedCacheFileIngestor {
	UpdateConceptsResultBo run(
			Long fileId,
			String eclKey,
			ETerminologyKind kind
	);
}
