package ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.nfs;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;

import ar.lamansys.sgx.shared.files.exception.FileServiceEnumException;
import ar.lamansys.sgx.shared.files.exception.FileServiceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NFSUtils {

	public final static String getSpaceLocation(Path path) {
		try {
			FileStore fs = Files.getFileStore(path);
			return String.format(
					"%s/%s",
					FileUtils.byteCountToDisplaySize(fs.getUsableSpace()),
					FileUtils.byteCountToDisplaySize(fs.getTotalSpace())
			);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return String.format("Error en la lectura del directorio %s", path);
	}

	public static void validateLocation(File file) {
		if (!file.exists())
			if (!file.mkdirs())
				throw new FileServiceException(
						FileServiceEnumException.CANNOT_CREATE_FOLDER,
						String.format("La carpeta %s no puede ser creada", file)
				);
	}

	public static void validateFreeSpace(File folder, long size) throws IOException {
		FileStore fs = Files.getFileStore(folder.toPath());
		if (fs.getUsableSpace() < size)
			throw new FileServiceException(FileServiceEnumException.INSUFFICIENT_STORAGE,
					String.format("La carpeta %s no tiene espacio suficiente (%s) para alojar el archivo de tamaño %s",
							folder,
							FileUtils.byteCountToDisplaySize(fs.getUsableSpace()),
							FileUtils.byteCountToDisplaySize(size)));
	}
}
