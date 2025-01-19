package ar.lamansys.sgx.shared.files;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.AppNode;
import ar.lamansys.sgx.shared.context.BeanUtil;
import ar.lamansys.sgx.shared.files.exception.FileServiceEnumException;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;
import ar.lamansys.sgx.shared.files.infrastructure.configuration.interceptors.FileErrorEvent;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileErrorInfo;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfo;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileInfoRepository;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BlobStorage;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BucketObjectInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class FileService {

	private static final String OUTPUT = "Output -> {}";
	private final BlobStorage blobStorage;
	private final FileInfoRepository repository;
	private final AppNode appNode;

	public FilePathBo buildCompletePath(String fileRelativePath){
		log.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
		FilePathBo path = blobStorage.buildPath(fileRelativePath);
		log.debug(OUTPUT, path);
		return path;
	}

	public String createFileName(String extension){
		String result = this.createUuid() + '.' + extension;
		log.debug(OUTPUT, result);
		return result;
	}

	public String createUuid(){
		String result = UUID.randomUUID().toString();
		log.debug(OUTPUT, result);
		return result;
	}

	public FileInfo transferMultipartFile(FilePathBo path, String uuid, String generatedFrom, MultipartFile file) {
		File dirPath = path.toFile();
		try {
			var info = blobStorage.put(path, FileContentBo.fromResource(file.getResource()));
			log.debug(OUTPUT, true);
			var fileInfoDB = buildFileInfo(uuid, generatedFrom, info, file.getOriginalFilename(), file.getContentType());
			return saveFileInfo(fileInfoDB);
		} catch (IOException e) {
			saveFileError(new FileErrorInfo(path.relativePath, String.format("transferMultipartFile error => %s", e), appNode.nodeId));
			log.error(e.toString());
			throw new FileServiceException(
					FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("El guardado del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e.getMessage())
			);
		}
	}

	public FileContentBo loadFileRelativePath(String relativeFilePath) {
		return loadFile(blobStorage.buildPath(relativeFilePath));
	}

	public FileContentBo loadFile(FilePathBo path) {
		try {
			return blobStorage.get(path);
		} catch (Exception e) {
			log.error(e.getMessage());
			saveFileError(new FileErrorInfo(
					path.relativePath,
					String.format("loadFileRelativePath error => %s", e.getMessage()),
					appNode.nodeId
			));
			throw new FileServiceException(
					FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("El guardado del siguiente archivo %s tuvo el siguiente error %s", path.relativePath, e)
			);
		}
	}

	public StoredFileBo loadStoredFile(Long fileId) {
		var fileInfoSaved = repository.findById(fileId);
		if (fileInfoSaved.isEmpty()) {
			throw new FileServiceException(
					FileServiceEnumException.NON_EXIST,
					String.format("El archivo con id %s no existe", fileId)
			);
		}
		FileInfo fileInfo = fileInfoSaved.get();
		FileContentBo resource = this.loadFileRelativePath(fileInfo.getRelativePath());
		return new StoredFileBo(
				resource,
				fileInfo.getContentType(),
				fileInfo.getName()
		);
	}

	public FileContentBo loadFile(Long fileId) {
		return loadStoredFile(fileId).resource;
	}

	public FileInfo saveStreamInPath(FilePathBo path, String uuid, String generatedFrom, boolean override,
									 FileContentBo content) {
		return this.saveStreamInPath(
				path,
				uuid,
				generatedFrom,
				override,
				content,
				path.toFile().getName(),
				parseToContentType(path.toFile().getName())
		);
	}

	public FileInfo saveStreamInPath(FilePathBo path, String uuid, String generatedFrom, boolean override, StoredFileBo storedFile) {
		return this.saveStreamInPath(
				path,
				uuid,
				generatedFrom,
				override,
				storedFile.resource,
				storedFile.filename,
				storedFile.getContentType()
		);
	}

	private FileInfo saveStreamInPath(
			FilePathBo path,
			String uuid,
			String generatedFrom,
			boolean override,
			FileContentBo content,
			String originalName,
			String contentType
	) {
		File dirPath = path.toFile();
		try {
			var info = blobStorage.put(path, content, override);
			var fileInfoDB = buildFileInfo(uuid, generatedFrom, info, originalName, contentType);
			return saveFileInfo(fileInfoDB);
		} catch (IOException e) {
			saveFileError(new FileErrorInfo(dirPath.getPath(), String.format("saveStreamInPath error => %s", e), appNode.nodeId));
			log.error(e.toString());
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("El guardado del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e));
		}
	}

	private static FileInfo buildFileInfo(String uuid, String generatedFrom, BucketObjectInfo objectInfo, String originalName, String contentType) {
		return new FileInfo(
				originalName,
				objectInfo.path.relativePath,
				contentType,
				objectInfo.size,
				uuid,
				objectInfo.checksum,
				generatedFrom
		);
	}

	private FileInfo saveFileInfo(FileInfo fileInfo) {
		return repository.save(fileInfo);
	}

	private static String parseToContentType(String fileName) {

		var contentType = URLConnection.guessContentTypeFromName(fileName);
		if (contentType != null) {
			return contentType;
		}
		if (fileName.endsWith(".csv")) {
			return "text/csv";
		}
		return "application/octet-stream";
	}

	public Optional<String> readFileAsString(FilePathBo path, Charset encoding) {

		try {
			return Optional.of(blobStorage.readFileAsString(path, encoding));
		} catch (IOException e) {
			log.error(e.getMessage());
			saveFileError(new FileErrorInfo(
					path.relativePath,
					String.format("readFileAsString error => %s", e),
					appNode.nodeId
			));
			return Optional.empty();
		}
	}

	private void saveFileError(FileErrorInfo fileErrorInfo) {
		BeanUtil.publishEvent(new FileErrorEvent(fileErrorInfo));
	}

	public boolean deleteFile(FilePathBo path) {
		blobStorage.delete(path);
		repository.deleteByRelativePath(path.relativePath);
		return true;
	}

	public boolean validateFileExists(FilePathBo path) {
		if (blobStorage.existFile(path))
			throw new FileServiceException(FileServiceEnumException.NON_EXIST, String.format("El archivo %s no existe", path.relativePath));
		return true;
	}

//	public void migrateFiles(StorageFacade fromStorage) {
//		Stream<FileInfo> streamToMigrate = repository.findAll().stream()
//			.filter(fileInfo -> fromStorage.fileExist(fileInfo.getRelativePath()));
//
//		streamToMigrate.forEach(
//				fileInfo -> {
//					if (streamFile.storageFacade.fileExist(fileInfo.getRelativePath())) {
//						System.out.println("EXISTE " + fileInfo.getRelativePath());
//						return;
//					}
//					System.out.println("PARA MIGRAR " + fileInfo.getRelativePath());
//					try {
//						streamFile.storageFacade.put(
//								fileInfo.getRelativePath(),
//								fromStorage.get(fileInfo.getRelativePath())
//						);
//					} catch (Exception e) {
//						log.error(e.getMessage(), e);
//					}
//
//				}
//		);
//	}
}
