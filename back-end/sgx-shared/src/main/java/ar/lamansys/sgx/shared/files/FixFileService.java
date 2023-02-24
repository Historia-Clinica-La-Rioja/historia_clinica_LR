package ar.lamansys.sgx.shared.files;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
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

	public String buildCompletePath(String fileRelativePath){
		log.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
		String path = blobStorage.buildPathAsString(fileRelativePath);
		log.debug(OUTPUT, path);
		return path;
	}


	private String parseToContentType(String fileName) {
		return URLConnection.guessContentTypeFromName(fileName);
	}


	private void saveFileError(FileErrorInfo fileErrorInfo) {
		BeanUtil.publishEvent(new FileErrorEvent(fileErrorInfo));
	}


	private static String getHash(String path) {
		log.debug("Input parameters -> path {}", path);
		String result;
		String algorithm = "SHA-256";
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] sha256Hash = md.digest(Files.readAllBytes(Paths.get(path)));
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
					String completePath = buildCompletePath(fileInfo.getRelativePath());
					fileInfo.setChecksum(getHash(completePath));
					try {
						fileInfo.setSize(Files.size(Paths.get(completePath)));
					}  catch (FileServiceException | IOException e){
						try {
							saveFileError(new FileErrorInfo(fileInfo.getRelativePath(), String.format("fixMetadata error => %s", e), appNode.nodeId));
							log.error(e.toString());
							fileInfo.setSize(Files.size(Paths.get(fileInfo.getOriginalPath())));
						} catch (IOException ex) {
							saveFileError(new FileErrorInfo(fileInfo.getOriginalPath(), String.format("fixMetadata error => %s", e), appNode.nodeId));
							log.error(e.toString());
							throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
									String.format("La lectura del siguiente archivo %s tuvo el siguiente error %s", fileInfo.getRelativePath(), e));
						}
					}
					fileInfo.setContentType(parseToContentType(fileInfo.getName()));
					return repository.save(fileInfo);
				});
	}

}
