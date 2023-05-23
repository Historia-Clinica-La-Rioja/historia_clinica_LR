package net.pladema.terminology.cache.infrastructure.output.repository;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import lombok.AllArgsConstructor;
import net.pladema.terminology.cache.infrastructure.output.SnomedCacheFileStorage;

@AllArgsConstructor
@Service
public class SnomedCacheFileStorageImpl implements SnomedCacheFileStorage {

	private final FileService fileService;

	@Override
	public FileInfo save(String url) {
		var fileContent = getFileContentBo(url);
		var uuid = fileService.createUuid();

		var randomPath = String.format("cache-csv/test/%s.csv", uuid);

		var path = fileService.buildCompletePath(randomPath);

		return fileService.saveStreamInPath(
				path,
				uuid,
				"CACHE-CSV",
				true,
				fileContent
		);
	}

	private static FileContentBo getFileContentBo(String url) {
		try {
			return FileContentBo.fromResource(new UrlResource(url));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
