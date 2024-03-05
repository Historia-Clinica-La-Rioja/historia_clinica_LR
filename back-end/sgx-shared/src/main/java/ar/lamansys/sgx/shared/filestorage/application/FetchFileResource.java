package ar.lamansys.sgx.shared.filestorage.application;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfoRepository;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.BlobLazyFileBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BlobStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class FetchFileResource {
	private final FileInfoRepository fileInfoRepository;
	private final BlobStorage blobStorage;


	public BlobLazyFileBo run(String relativePath) {
		var optFile = fileInfoRepository.findByRelativePath(relativePath);
		if (optFile.isEmpty())
			return null;
		var fileInfo = optFile.get();
		return getResourceResponseEntity(fileInfo);
	}

	public BlobLazyFileBo run(Long id) {
		var optFile = fileInfoRepository.findById(id);
		if (optFile.isEmpty())
			return null;
		var fileInfo = optFile.get();
		return getResourceResponseEntity(fileInfo);
	}

	private BlobLazyFileBo getResourceResponseEntity(FileInfo fileInfo) {
		FilePathBo path = blobStorage.buildPath(fileInfo.getRelativePath());
		return new BlobLazyFileBo(
				() -> {
					if (!blobStorage.existFile(path)) {
						log.warn("Archivo no encontrado {}", path);
						throw new BucketObjectNotFoundException(path);
					}
					try {
						return blobStorage.get(path);
					} catch (Exception e) {
						log.error("Obteniendo archivo {}", path);
						throw new BucketObjectAccessException(path);
					}
				},
				MediaType.parseMediaType(fileInfo.getContentType()),
				fileInfo.getName()
		);

	}


}
