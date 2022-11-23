package ar.lamansys.sgx.shared.files;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.AppNode;
import ar.lamansys.sgx.shared.context.BeanUtil;
import ar.lamansys.sgx.shared.files.exception.FileServiceEnumException;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;
import ar.lamansys.sgx.shared.files.infrastructure.configuration.interceptors.FileErrorEvent;
import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileErrorInfo;

@Component
public class FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    private static final String OUTPUT = "Output -> {}";

    private final StreamFile streamFile;

	private final FileConfiguration fileConfiguration;
	private final AppNode appNode;
    public FileService(StreamFile streamFile, FileConfiguration fileConfiguration,
					   AppNode appNode){
        this.streamFile = streamFile;
		this.fileConfiguration = fileConfiguration;
		this.appNode = appNode;
	}
	public final String getSpaceDocumentLocation() {
		try {
			FileStore fs = Files.getFileStore(fileConfiguration.getDocumentsLocation().toPath());
			return String.format("%s/%s", FileUtils.byteCountToDisplaySize(fs.getUsableSpace()), FileUtils.byteCountToDisplaySize(fs.getTotalSpace()));
		} catch (IOException e) {
			LOG.error(e.toString());
			saveFileError(new FileErrorInfo(fileConfiguration.getDocumentsLocation().toPath().toString(), String.format("getSpaceDocumentLocation error => %s", e), appNode.nodeId));
			return String.format("Error en la lectura del directorio %s", fileConfiguration.getDocumentsLocation().toPath());
		}
	}
	public final String getSpaceMultipartLocation() {
		try {
			FileStore fs = Files.getFileStore(fileConfiguration.getMultipartLocation().toPath());
			return String.format("%s/%s", FileUtils.byteCountToDisplaySize(fs.getUsableSpace()), FileUtils.byteCountToDisplaySize(fs.getTotalSpace()));
		} catch (IOException e) {
			LOG.error(e.toString());
			saveFileError(new FileErrorInfo(fileConfiguration.getMultipartLocation().toPath().toString(), String.format("getSpaceMultipartLocation error => %s", e), appNode.nodeId));
			return String.format("Error en la lectura del directorio %s", fileConfiguration.getMultipartLocation().toPath());
		}
	}

    public String buildRelativePath(String fileRelativePath){
        LOG.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
        String path = streamFile.buildPathAsString(fileRelativePath);
        LOG.debug(OUTPUT, path);
        return path;
    }

    public String createFileName(String extension){
        String result = UUID.randomUUID().toString() + '.' + extension;
        LOG.debug(OUTPUT, result);
        return result;
    }

    public boolean transferMultipartFile(String path, MultipartFile file) throws IOException {
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
            LOG.debug(OUTPUT, true);
            return true;
        }  catch (FileServiceException e) {
			saveFileError(new FileErrorInfo(path, String.format("transferMultipartFile error => %s", e), appNode.nodeId));
			throw e;
		}
		catch (IOException e) {
			saveFileError(new FileErrorInfo(path, String.format("transferMultipartFile error => %s", e), appNode.nodeId));
			LOG.error(e.toString());
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("El guardado del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e.getMessage()));
        }
    }

    public Resource loadFile(String relativeFilePath) {
        Path path = streamFile.buildPath(relativeFilePath);
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
			saveFileError(new FileErrorInfo(relativeFilePath, String.format("loadFile error => %s", e), appNode.nodeId));
			LOG.error(e.toString());
        }
        return null;
    }
	public boolean saveStreamInPath(String path, boolean override, ByteArrayOutputStream byteArrayOutputStream) {
		File dirPath = new File(path);
		try {
			streamFile.saveFileInDirectory(path, override, byteArrayOutputStream);
		} catch (IOException e) {
			saveFileError(new FileErrorInfo(dirPath.getPath(), String.format("saveStreamInPath error => %s", e), appNode.nodeId));
			LOG.error(e.toString());
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("El guardado del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e.getMessage()));
		}
		return true;
	}

	public String readFileAsString(String path, Charset encoding) {
		File dirPath = new File(path);
		try {
			return streamFile.readFileAsString(path, encoding);
		} catch (IOException e) {
			saveFileError(new FileErrorInfo(dirPath.getPath(), String.format("readFileAsString error => %s", e), appNode.nodeId));
			LOG.error(e.toString());
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("La lectura del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e.getMessage()));
		}
	}

	public ByteArrayInputStream readStreamFromPath(String path) {
		LOG.debug("Input parameters -> path {}", path);
		File dirPath = new File(path);
		try {
			Path pdfPath = Paths.get(path);
			byte[] pdf = Files.readAllBytes(pdfPath);
			LOG.debug("Output -> path {}", path);
			return new ByteArrayInputStream(pdf);
		}  catch (IOException e){
			saveFileError(new FileErrorInfo(dirPath.getPath(), String.format("readStreamFromPath error => %s", e), appNode.nodeId));
			LOG.error(e.toString());
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("La lectura del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e.getMessage()));
		}
	}

	private void saveFileError(FileErrorInfo fileErrorInfo) {
		BeanUtil.publishEvent(new FileErrorEvent(fileErrorInfo));
	}

	public boolean deleteFile(String path) {
        return streamFile.deleteFileInDirectory(path);
    }

}
