package ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.files.FileConfiguration;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.nfs.NFSUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BlobStorage {

	private final FileConfiguration fileConfiguration;
    public BlobStorage(FileConfiguration fileConfiguration){
        super();
		this.fileConfiguration = fileConfiguration;
	}

    public String buildPathAsString(String relativeFilePath) {
        return getRootDirectory() + relativeFilePath;
    }

    public Path buildPath(String relativeFilePath){
        return Paths.get(getRootDirectory(), relativeFilePath);
    }

    public boolean existFile(String path) {
        File file = new File(path);
        var result = file.isFile();
		log.debug("File in {} exists? => {}", path, result);
		return result;
    }

    public boolean saveFileInDirectory(String path, boolean override, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        boolean fileCreated;
        boolean directoryCreated = true;

        File file = new File(path);
        if(!file.getParentFile().exists())
            directoryCreated = file.getParentFile().mkdirs();

        boolean overrideFile = override ? true : !file.exists();
        if(directoryCreated && overrideFile) {
            fileCreated = override ? true : file.createNewFile();

            try(OutputStream outputStream = new FileOutputStream(file)){
                outputStream.write(byteArrayOutputStream.toByteArray());
            }
			log.debug("File loaded in directory -> {}", fileCreated);
            return fileCreated;
        }
        return false;
    }

    public String readFileAsString(String path, Charset charset) throws IOException{
		log.debug("Input parameter -> path {}", path);
        String result = null;
        ByteArrayInputStream is = reader(path);
        int numberOfBytes = is.available();
        byte[] bytes = new byte[numberOfBytes];
        is.read(bytes, 0, numberOfBytes);
        result = new String(bytes, charset);
		log.debug("Output -> {}", dataToString(result));
        return result;
    }

    public boolean deleteFileInDirectory(String path) {
        File file = new File(path);
        return file.delete();
    }

    private ByteArrayInputStream reader(String path) throws IOException{
		log.debug("Input parameters -> path {}", path);
        Path filePath = Paths.get(path);
        byte[] data = Files.readAllBytes(filePath);
        log.debug("Output -> data {}", bytesToString(data));
        return new ByteArrayInputStream(data);
    }

    private String getRootDirectory() {
        return fileConfiguration.getDocumentsLocation().getPath();
    }

    private String dataToString(String data){
        return "(String length='" + (data.length()) + '\'' + ')';
    }

    private String bytesToString(byte[] data){
        return "(Byte array length='" + (data.length) + '\'' + ')';
    }

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
