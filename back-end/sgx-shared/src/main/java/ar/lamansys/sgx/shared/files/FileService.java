package ar.lamansys.sgx.shared.files;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.AppNode;
import ar.lamansys.sgx.shared.context.BeanUtil;
import ar.lamansys.sgx.shared.files.exception.FileServiceEnumException;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;
import ar.lamansys.sgx.shared.files.infrastructure.configuration.interceptors.FileErrorEvent;
import ar.lamansys.sgx.shared.files.infrastructure.input.rest.backoffice.dto.FileInfoDto;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileErrorInfo;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Component
public class FileService {

	private static final String OUTPUT = "Output -> {}";

	private final StreamFile streamFile;
	private final FileConfiguration fileConfiguration;

	private final FileInfoRepository repository;
	private final AppNode appNode;

	public final String getSpaceDocumentLocation() {
		try {
			FileStore fs = Files.getFileStore(fileConfiguration.getDocumentsLocation().toPath());
			return String.format("%s/%s", FileUtils.byteCountToDisplaySize(fs.getUsableSpace()), FileUtils.byteCountToDisplaySize(fs.getTotalSpace()));
		} catch (IOException e) {
			log.error(e.toString());
			saveFileError(new FileErrorInfo(fileConfiguration.getDocumentsLocation().toPath().toString(), String.format("getSpaceDocumentLocation error => %s", e), appNode.nodeId));
			return String.format("Error en la lectura del directorio %s", fileConfiguration.getDocumentsLocation().toPath());
		}
	}
	public final String getSpaceMultipartLocation() {
		try {
			FileStore fs = Files.getFileStore(fileConfiguration.getMultipartLocation().toPath());
			return String.format("%s/%s", FileUtils.byteCountToDisplaySize(fs.getUsableSpace()), FileUtils.byteCountToDisplaySize(fs.getTotalSpace()));
		} catch (IOException e) {
			log.error(e.toString());
			saveFileError(new FileErrorInfo(fileConfiguration.getMultipartLocation().toPath().toString(), String.format("getSpaceMultipartLocation error => %s", e), appNode.nodeId));
			return String.format("Error en la lectura del directorio %s", fileConfiguration.getMultipartLocation().toPath());
		}
	}

	public String buildCompletePath(String fileRelativePath){
		log.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
		String path = streamFile.buildPathAsString(fileRelativePath);
		log.debug(OUTPUT, path);
		return path;
	}

	public String createFileName(String extension){
		String result = UUID.randomUUID().toString() + '.' + extension;
		log.debug(OUTPUT, result);
		return result;
	}

	public FileInfo transferMultipartFile(String partialPath, String uuid, String generatedFrom, MultipartFile file) throws IOException {
		String path = buildCompletePath(partialPath);
		File dirPath = new File(path);
		try {
			if (!dirPath.getParentFile().exists())
				if (!dirPath.getParentFile().mkdirs())
					throw new FileServiceException(FileServiceEnumException.CANNOT_CREATE_FOLDER, String.format("La carpeta %s no puede ser creada", dirPath.getParentFile()));
			FileStore fs = Files.getFileStore(dirPath.getParentFile().toPath());
			if (fs.getUsableSpace() < file.getSize())
				throw new FileServiceException(FileServiceEnumException.INSUFFICIENT_STORAGE,
						String.format("La carpeta %s no tiene espacio suficiente (%s) para alojar el archivo de tamaño %s",
								dirPath.getParentFile(),
								FileUtils.byteCountToDisplaySize(fs.getUsableSpace()),
								FileUtils.byteCountToDisplaySize(file.getSize())));
			file.transferTo(dirPath);
			log.debug(OUTPUT, true);
			return saveFileInfo(partialPath, uuid, generatedFrom, getHash(path), file);
		}  catch (FileServiceException e) {
			saveFileError(new FileErrorInfo(path, String.format("transferMultipartFile error => %s", e.getMessage()), appNode.nodeId));
			throw e;
		}
		catch (IOException e) {
			saveFileError(new FileErrorInfo(path, String.format("transferMultipartFile error => %s", e), appNode.nodeId));
			log.error(e.toString());
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("El guardado del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e.getMessage()));
		}
	}

	private FileInfo saveFileInfo(String path, String uuid, String generatedFrom, String checksum, MultipartFile file) {
		return repository.save(new FileInfo(
				file.getOriginalFilename(),
				path,
				file.getContentType(),
				file.getSize(),
				uuid,
				checksum,
				generatedFrom));
	}

	public Resource loadFileRelativePath(String relativeFilePath) {
		Path path = streamFile.buildPath(relativeFilePath);
		try {
			validateRelativePath(relativeFilePath);
		} catch (FileServiceException e) {
			saveFileError(new FileErrorInfo(relativeFilePath, String.format("loadFileRelativePath error => %s", e.getMessage()), appNode.nodeId));
			log.error(e.getMessage());
			throw e;
		}
		try {
			return new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			saveFileError(new FileErrorInfo(relativeFilePath, String.format("loadFile error => %s", e), appNode.nodeId));
			log.error(e.toString());
		}
		return null;
	}

	public Resource loadFileFromAbsolutePath(String absolutePath) {
		Path path = Paths.get(absolutePath);
		try {
			validateAbsolutePath(path.toString());
		} catch (FileServiceException e) {
			saveFileError(new FileErrorInfo(absolutePath, String.format("loadFileFromAbsolutePath error => %s", e.getMessage()), appNode.nodeId));
			log.error(e.getMessage());
			throw e;
		}
		try {
			return new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			saveFileError(new FileErrorInfo(absolutePath, e.getMessage(), appNode.nodeId));
			log.error(e.getMessage());
		}
		return null;
	}
	public FileInfo saveStreamInPath(String partialPath, String uuid, String generatedFrom, boolean override,
									 ByteArrayOutputStream byteArrayOutputStream) {
		String path = buildCompletePath(partialPath);
		File dirPath = new File(path);
		try {
			streamFile.saveFileInDirectory(path, override, byteArrayOutputStream);
			return saveFileInfo(partialPath, uuid, generatedFrom, getHash(path), dirPath);
		} catch (IOException e) {
			saveFileError(new FileErrorInfo(dirPath.getPath(), String.format("saveStreamInPath error => %s", e), appNode.nodeId));
			log.error(e.toString());
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("El guardado del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e));
		}
	}

	private FileInfo saveFileInfo(String path, String uuid, String generatedFrom, String checksum,  File file) throws IOException {
		return repository.save(new FileInfo(
				file.getName(),
				path,
				parseToContentType(file.getName()),
				Files.size(file.toPath()),
				uuid,
				checksum,
				generatedFrom));
	}

	private String parseToContentType(String fileName) {
		return URLConnection.guessContentTypeFromName(fileName);
	}

	public String readFileAsString(String path, Charset encoding) {
		File dirPath = new File(path);
		try {
			return streamFile.readFileAsString(path, encoding);
		} catch (IOException e) {
			saveFileError(new FileErrorInfo(dirPath.getPath(), String.format("readFileAsString error => %s", e), appNode.nodeId));
			log.error(e.toString());
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("La lectura del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e));
		}
	}

	public ByteArrayInputStream readStreamFromRelativePath(String partialPath) {
		log.debug("Input parameters -> partialPath {}", partialPath);
		String path = buildCompletePath(partialPath);
		return readStreamFromAbsolutePath(path);
	}

	public ByteArrayInputStream readStreamFromAbsolutePath(String absolutePath) {
		log.debug("Input parameters -> absolutePath {}", absolutePath);
		File dirPath = new File(absolutePath);
		try {
			Path pdfPath = Paths.get(absolutePath);
			byte[] pdf = Files.readAllBytes(pdfPath);
			log.debug("Output -> path {}", absolutePath);
			return new ByteArrayInputStream(pdf);
		}  catch (IOException e){
			saveFileError(new FileErrorInfo(dirPath.getPath(), String.format("readStreamFromPath error => %s", e), appNode.nodeId));
			log.error(e.toString());
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("La lectura del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e));
		}
	}

	private void saveFileError(FileErrorInfo fileErrorInfo) {
		BeanUtil.publishEvent(new FileErrorEvent(fileErrorInfo));
	}

	public boolean deleteFile(String partialPath) {
		String completePath = buildCompletePath(partialPath);
		if (!streamFile.deleteFileInDirectory(completePath))
			return false;
		repository.deleteByRelativePath(partialPath);
		return true;
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

	public long getFileSize(FileInfoDto fileInfo) throws IOException {
		String completePath = buildCompletePath(fileInfo.getRelativePath());
		Long unknownSize = -1L;
		if (!unknownSize.equals(fileInfo.getSize()))
			return fileInfo.getSize();
		try {
			return Files.size(Paths.get(completePath));
		}  catch (FileServiceException | IOException e){
			saveFileError(new FileErrorInfo(fileInfo.getRelativePath(), String.format("getFileSize error => %s", e), appNode.nodeId));
			log.error(e.toString());
			try {
				return Files.size(Paths.get(fileInfo.getOriginalPath()));
			}  catch (IOException e1){
				saveFileError(new FileErrorInfo(fileInfo.getOriginalPath(), String.format("getFileSize error => %s", e1), appNode.nodeId));
				log.error(e1.toString());
				throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
						String.format("La lectura del siguiente archivo %s tuvo el siguiente error %s", fileInfo.getRelativePath(), e));
			}
		}
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

	public boolean validateRelativePath(String relativePath) {
		String path = buildCompletePath(relativePath);
		return validateAbsolutePath(path);
	}

	public boolean validateAbsolutePath(String absolutePath) {
		File dirPath = new File(absolutePath);
		if (!dirPath.exists())
			throw new FileServiceException(FileServiceEnumException.NON_EXIST, String.format("El archivo %s no existe", dirPath));
		return true;
	}
}
