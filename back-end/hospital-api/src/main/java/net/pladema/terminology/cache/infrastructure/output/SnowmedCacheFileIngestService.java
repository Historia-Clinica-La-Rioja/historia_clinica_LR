package net.pladema.terminology.cache.infrastructure.output;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.pladema.terminology.cache.infrastructure.output.repository.SnomedCacheFileRepository;
import net.pladema.terminology.cache.infrastructure.output.repository.entity.SnomedCacheFile;

@AllArgsConstructor
@Service
public class SnowmedCacheFileIngestService {
	private final SnomedCacheFileRepository snomedCacheFileRepository;

	public Optional<SnomedCacheFile> findFirstToIngest() {
		return snomedCacheFileRepository
				.findToIngestByAge(Pageable.ofSize(1))
				.stream().findFirst();
	}

	public void ingested(SnomedCacheFile cacheFile, Integer conceptsLoaded, Integer erroneousConcepts) {
		cacheFile.setConceptsLoaded(conceptsLoaded);
		cacheFile.setConceptsErroneous(erroneousConcepts);
		cacheFile.setIngestedError(null);
		saveSnomedCacheFile(cacheFile);
	}

	public void ingested(SnomedCacheFile cacheFile, String message) {
		cacheFile.setIngestedError(message);
		saveSnomedCacheFile(cacheFile);
	}

	private void saveSnomedCacheFile(SnomedCacheFile cacheFile) {
		cacheFile.setIngestedOn(LocalDateTime.now());
		snomedCacheFileRepository.save(cacheFile);
	}

	public void processing(SnomedCacheFile cacheFile) {
		cacheFile.setIngestedError("Ingesting...");
		snomedCacheFileRepository.save(cacheFile);
	}

}
