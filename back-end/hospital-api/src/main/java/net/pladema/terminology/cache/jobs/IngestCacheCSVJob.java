package net.pladema.terminology.cache.jobs;

import java.util.Date;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.terminology.cache.infrastructure.output.SnomedCacheFileIngestor;
import net.pladema.terminology.cache.infrastructure.output.SnowmedCacheFileIngestService;
import net.pladema.terminology.cache.infrastructure.output.repository.entity.SnomedCacheFile;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
		value="scheduledjobs.ingest-csv.enabled",
		havingValue = "true",
		matchIfMissing = true)
public class IngestCacheCSVJob {

	private final SnowmedCacheFileIngestService snowmedCacheFileIngestService;
	private final SnomedCacheFileIngestor snomedCacheFileIngestor;

	@Scheduled(cron = "${scheduledjobs.ingest-csv.cron}")
	@SchedulerLock(name = "IngestCacheCSVJob")
	public void execute() {
		log.warn("Scheduled IngestCacheCSVJob starting at {}", new Date());

		var cacheFileToIngestOpt = snowmedCacheFileIngestService.findFirstToIngest();
		if (cacheFileToIngestOpt.isPresent()) {
			ingest(cacheFileToIngestOpt.get());
		} else {
			log.debug("No hay CSV para cargar");
		}

		log.warn("Scheduled IngestCacheCSVJob done at {}", new Date());
	}

	private void ingest(SnomedCacheFile cacheFileToIngest) {
		snowmedCacheFileIngestService.processing(cacheFileToIngest);
		try {
			var updateConceptsResultBo = snomedCacheFileIngestor.run(cacheFileToIngest.getFileId(), cacheFileToIngest.getEcl(), cacheFileToIngest.getKind());
			snowmedCacheFileIngestService.ingested(cacheFileToIngest, updateConceptsResultBo.conceptsLoaded, updateConceptsResultBo.erroneousConcepts);
		} catch (Exception e) {
			snowmedCacheFileIngestService.ingested(cacheFileToIngest, e.getMessage());
		}
	}
}
