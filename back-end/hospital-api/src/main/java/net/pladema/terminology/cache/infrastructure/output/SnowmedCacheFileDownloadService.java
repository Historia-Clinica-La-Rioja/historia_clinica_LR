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
public class SnowmedCacheFileDownloadService {
	private final SnomedCacheFileRepository snomedCacheFileRepository;

	public Optional<SnomedCacheFile> findFirstToDownload() {
		var cacheFileToDownloadOpt = snomedCacheFileRepository.findToDownloadByAge(Pageable.ofSize(1));
		return cacheFileToDownloadOpt.stream().findFirst();
	}

	public void downloaded(SnomedCacheFile cacheFile, Long fileId) {
		cacheFile.setFileId(fileId);
		cacheFile.setDownloadedError(null);
		saveSnomedCacheFile(cacheFile);
	}

	public void downloaded(SnomedCacheFile cacheFile, String message) {
		cacheFile.setDownloadedError(message);
		saveSnomedCacheFile(cacheFile);
	}

	private void saveSnomedCacheFile(SnomedCacheFile cacheFile) {
		cacheFile.setDownloadedOn(LocalDateTime.now());
		snomedCacheFileRepository.save(cacheFile);
	}


	public void processing(SnomedCacheFile cacheFile) {
		cacheFile.setDownloadedError("Downloading...");
		snomedCacheFileRepository.save(cacheFile);
	}
}
