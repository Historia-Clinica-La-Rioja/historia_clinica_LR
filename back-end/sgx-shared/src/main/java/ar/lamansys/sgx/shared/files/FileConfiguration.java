package ar.lamansys.sgx.shared.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import ar.lamansys.sgx.shared.files.exception.FileServiceEnumException;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
@Getter
public class FileConfiguration {

	private final File documentsLocation;
	private final File multipartLocation;
	private final Long minimumFreeSpace;
	public FileConfiguration(@Value("${internment.document.directory}") String documentsLocation,
							 @Value("${spring.servlet.multipart.location}") String multipartLocation,
							 @Value("${files.folder.freespace.minimum}") Long minimumFreeSpace) throws IOException {
		this.documentsLocation = new File(documentsLocation);
		this.multipartLocation = new File(multipartLocation);
		checkFolder(this.documentsLocation, minimumFreeSpace);
		checkFolder(this.multipartLocation, minimumFreeSpace);
		this.minimumFreeSpace = minimumFreeSpace;
	}
	private void checkFolder(File file, Long minimumFreeSpace) throws IOException {
		if  (!file.isDirectory()) {
			if (!file.exists()) {
				if (!file.mkdirs())
					throw new FileServiceException(FileServiceEnumException.CANNOT_CREATE_FOLDER, String.format("La carpeta %s no puede ser creada", file));
			}
			else throw new FileServiceException(FileServiceEnumException.IS_NOT_DIRECTORY, String.format("La ruta %s no es un directorio", file.getAbsolutePath()));
		}
		if (!Files.isWritable(file.toPath()))
			throw new FileServiceException(FileServiceEnumException.WRITE_INVALID, String.format("La carpeta %s no es valida para escritura", file.getAbsolutePath()));
		if (!Files.isReadable(file.toPath()))
			throw new FileServiceException(FileServiceEnumException.READ_INVALID, String.format("La carpeta %s no es valida para lectura", file.getAbsolutePath()));
		log.debug("La carpeta {} esta disponible para escritura/lectura", file.getAbsolutePath());
		FileStore fs = Files.getFileStore(file.toPath());
		if (fs.getUsableSpace() <= minimumFreeSpace)
			throw new FileServiceException(FileServiceEnumException.INSUFFICIENT_STORAGE,
					String.format("La carpeta %s no cumple con el mínimo de espacio => esperado (%s), actual (%s) ",
							file,
							FileUtils.byteCountToDisplaySize(minimumFreeSpace),
							FileUtils.byteCountToDisplaySize(fs.getUsableSpace())));
	}
}