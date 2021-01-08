package net.pladema.sgx.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    private static final String OUTPUT = "Output -> {}";

    private final StreamFile streamFile;

    public FileService(StreamFile streamFile){
        this.streamFile = streamFile;
    }

    public String buildPath(String fileRelativePath){
        LOG.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
        String path = streamFile.buildPath(fileRelativePath);
        LOG.debug(OUTPUT, path);
        return path;
    }

    public String createFileName(String extension){
        String result = UUID.randomUUID().toString() + '.' + extension;
        LOG.debug(OUTPUT, result);
        return result;
    }

    public boolean saveFile(String path, MultipartFile file) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.writeBytes(file.getBytes());
            boolean result = streamFile.saveFileInDirectory(path, os);
            LOG.debug(OUTPUT, result);
            return result;
        } catch (IOException e) {
            LOG.error("Cannot save file at {}", path, e);
            return false;
        }
    }

}
