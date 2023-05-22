package ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.nfs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BlobStorage;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BucketObjectInfo;
import ar.lamansys.sgx.shared.stats.TimeProfilingUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
@ConditionalOnProperty(value="app.files.mode", havingValue="nfs",matchIfMissing=true)
public class NFSBlobStorage implements BlobStorage {
	private final FileConfiguration fileConfiguration;

	@Override
	public FilePathBo buildPath(String relativeFilePath) {
		return new FilePathBo(
				fileConfiguration.getDocumentsLocation().getPath(),
				relativeFilePath
		);
	}

	@Override
	public boolean existBucket() {
		return fileConfiguration.getDocumentsLocation().isDirectory();
	}

	@Override
	public boolean existFile(FilePathBo path) {
		return path
				.toFile()
				.isFile();

	}

	@Override
	public BucketObjectInfo put(FilePathBo path, FileContentBo object) throws IOException {
		var saveFileStat = TimeProfilingUtil.start("IO save");

		NFSUtils.validateLocation(path.fullPath.getParent().toFile());
		NFSUtils.validateFreeSpace(path.fullPath.getParent().toFile(), object.size);

		copyInputStreamToFile(object.stream, path.fullPath.toFile());

		saveFileStat.done(path.relativePath);

		return new BucketObjectInfo(
				path,
				object.size,
				getHash(path)
		);
	}

	@Override
	public BucketObjectInfo getInfo(FilePathBo path) {
		return new BucketObjectInfo(
				path,
				path.toFile().length(),
				getHash(path)
		);
	}

	@Override
	public void delete(FilePathBo relativePath) {

	}

	@Override
	public FileContentBo get(FilePathBo path) throws IOException {
		log.debug("Input parameters -> path {}", path);

		byte[] data = Files.readAllBytes(path.fullPath);
		return FileContentBo.fromBytes(data);
	}




	private static void copyInputStreamToFile(InputStream inputStream, File file)
			throws IOException {

		// append = false
		try (OutputStream output = new FileOutputStream(file, false)) {
			inputStream.transferTo(output);
		}

	}

	private static String getHash(FilePathBo path) {
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
		log.debug("Hash {} for file {}", result, path);
		return result;
	}

	@Override
	public Map<String, Object> status() {
		return Map.of(
				"Espacio libre/espacio total asignado a los documentos",
				NFSUtils.getSpaceLocation(
						fileConfiguration.getDocumentsLocation().toPath()
				),
				"Espacio libre/espacio total asignado a los multipartfiles",
				NFSUtils.getSpaceLocation(
						fileConfiguration.getMultipartLocation().toPath()
				)
		);
	}
}
