package net.pladema.terminology.cache.jobs;

import java.util.Date;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.terminology.cache.infrastructure.output.SnomedCacheFileStorage;
import net.pladema.terminology.cache.infrastructure.output.SnowmedCacheFileDownloadService;
import net.pladema.terminology.cache.infrastructure.output.repository.entity.SnomedCacheFile;

@Service
@Slf4j
@ConditionalOnProperty(
		value="scheduledjobs.download-csv.enabled",
		havingValue = "true",
		matchIfMissing = true)
public class DownloadCacheCSVJob {

    private final SnowmedCacheFileDownloadService snowmedCacheFileDownloadService;
	private final SnomedCacheFileStorage snomedCacheFileStorage;

	public DownloadCacheCSVJob(SnowmedCacheFileDownloadService snowmedCacheFileDownloadService, SnomedCacheFileStorage snomedCacheFileStorage) {
		this.snowmedCacheFileDownloadService = snowmedCacheFileDownloadService;
		this.snomedCacheFileStorage = snomedCacheFileStorage;
	}

	@Scheduled(cron = "${scheduledjobs.download-csv.cron}")
	@SchedulerLock(name = "DownloadCacheCSVJob")
    public void execute() {
        log.warn("Scheduled DownloadCacheCSVJob starting at {}", new Date());

		var cacheFileToDownloadOpt = snowmedCacheFileDownloadService.findFirstToDownload();
		if (cacheFileToDownloadOpt.isPresent()) {
			download(cacheFileToDownloadOpt.get());
		} else {
			log.debug("No hay CSV para descargar");
		}

		log.warn("Scheduled DownloadCacheCSVJob done at {}", new Date());
    }

	private void download(SnomedCacheFile cacheFileToDownload) {
		snowmedCacheFileDownloadService.processing(cacheFileToDownload);
		try {
			var fileInfo = snomedCacheFileStorage.save(cacheFileToDownload.getUrl());
			snowmedCacheFileDownloadService.downloaded(cacheFileToDownload, fileInfo.getId());
		} catch (Exception e) {
			snowmedCacheFileDownloadService.downloaded(cacheFileToDownload, e.getMessage());
		}
	}


}
