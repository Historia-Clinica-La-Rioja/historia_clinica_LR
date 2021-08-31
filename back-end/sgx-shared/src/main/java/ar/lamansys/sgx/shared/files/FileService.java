package ar.lamansys.sgx.shared.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    private static final String OUTPUT = "Output -> {}";

    private final StreamFile streamFile;

    public FileService(StreamFile streamFile){
        this.streamFile = streamFile;
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

    public boolean saveFile(String path, boolean override, MultipartFile file) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.writeBytes(file.getBytes());
            boolean result = streamFile.saveFileInDirectory(path, override, os);
            LOG.debug(OUTPUT, result);
            return result;
        } catch (IOException e) {
            LOG.error("Cannot save file at {}", path, e);
            return false;
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
