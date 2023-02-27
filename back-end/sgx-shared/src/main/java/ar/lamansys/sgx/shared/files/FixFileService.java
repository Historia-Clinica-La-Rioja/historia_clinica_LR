package ar.lamansys.sgx.shared.files;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.AppNode;
import ar.lamansys.sgx.shared.context.BeanUtil;
import ar.lamansys.sgx.shared.files.exception.FileServiceEnumException;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;
import ar.lamansys.sgx.shared.files.infrastructure.configuration.interceptors.FileErrorEvent;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileErrorInfo;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfoRepository;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BlobStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class FixFileService {

	private static final String OUTPUT = "Output -> {}";

	private final BlobStorage blobStorage;

	private final FileInfoRepository repository;
	private final AppNode appNode;

	private String parseToContentType(String fileName) {
		return URLConnection.guessContentTypeFromName(fileName);
	}


	private void saveFileError(FileErrorInfo fileErrorInfo) {
		BeanUtil.publishEvent(new FileErrorEvent(fileErrorInfo));
	}


	protected static String getHash(FilePathBo path) {
		log.debug("Input parameters -> path {}", path);
		String result;
		String algorithm = "SHA-256";
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] sha256Hash = md.digest(Files.readAllBytes(path.fullPath));
			result = Base64.getEncoder().encodeToString(sha256Hash);
		} catch (NoSuchAlgorithmException e) {
			log.error("Algorithm doesn't exist -> {} ",algorithm);
			result = null;
		}
		catch (IOException e) {
			log.error("Error with path file {} ", path, e);
			result = null;
		}
		log.debug(OUTPUT, result);
		return result;
	}


	public void fixMetadata(Long id) {
		repository.findById(id)
				.map(fileInfo -> {
					var path = blobStorage.buildPath(fileInfo.getRelativePath());
					fileInfo.setRelativePath(path.relativePath);
					try {
						var objectInfo = blobStorage.getInfo(path);
						fileInfo.setChecksum(objectInfo.checksum);
						fileInfo.setSize(objectInfo.size);
					}  catch (FileServiceException | IOException e){
						saveFileError(new FileErrorInfo(fileInfo.getRelativePath(), String.format("fixMetadata error => %s", e), appNode.nodeId));
						log.error(e.toString());
						throw new FileServiceException(
								FileServiceEnumException.SAVE_IOEXCEPTION,
								String.format("La lectura del siguiente archivo %s tuvo el siguiente error %s", fileInfo.getRelativePath(), e)
						);

					}
					fileInfo.setContentType(parseToContentType(fileInfo.getName()));
					return repository.save(fileInfo);
				});
	}

}
