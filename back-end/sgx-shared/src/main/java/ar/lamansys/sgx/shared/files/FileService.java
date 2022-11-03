package ar.lamansys.sgx.shared.files;

import ar.lamansys.sgx.shared.files.exception.FileServiceEnumException;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    private static final String OUTPUT = "Output -> {}";

    private final StreamFile streamFile;

	private final FileConfiguration fileConfiguration;
    public FileService(StreamFile streamFile, FileConfiguration fileConfiguration){
        this.streamFile = streamFile;
		this.fileConfiguration = fileConfiguration;
	}
	public final String getSpaceDocumentLocation() {
		try {
			FileStore fs = Files.getFileStore(fileConfiguration.getDocumentsLocation().toPath());
			return String.format("%s/%s", FileUtils.byteCountToDisplaySize(fs.getUsableSpace()), FileUtils.byteCountToDisplaySize(fs.getTotalSpace()));
		} catch (IOException e) {
			return String.format("Error en la lectura del directorio %s", fileConfiguration.getDocumentsLocation().toPath());
		}
	}
	public final String getSpaceMultipartLocation() {
		try {
			FileStore fs = Files.getFileStore(fileConfiguration.getMultipartLocation().toPath());
			return String.format("%s/%s", FileUtils.byteCountToDisplaySize(fs.getUsableSpace()), FileUtils.byteCountToDisplaySize(fs.getTotalSpace()));
		} catch (IOException e) {
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

    public boolean saveFile(String path, MultipartFile file) throws IOException {
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
        } catch (IOException e) {
			throw new FileServiceException(FileServiceEnumException.SAVE_IOEXCEPTION,
					String.format("El guardado del siguiente archivo %s tuvo el siguiente error %s", dirPath.getAbsolutePath(), e.getMessage()));
        }
    }

    public Resource loadFile(String relativeFilePath) {
        Path path = streamFile.buildPath(relativeFilePath);
        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteFile(String path) {
        return streamFile.deleteFileInDirectory(path);
    }
}
