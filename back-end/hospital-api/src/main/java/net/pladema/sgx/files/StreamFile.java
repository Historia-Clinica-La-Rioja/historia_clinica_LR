package net.pladema.sgx.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Paths;

@Component
public class StreamFile {

    private static final Logger LOG = LoggerFactory.getLogger(StreamFile.class);

    @Value("${internment.document.directory:temp}")
    private String rootDirectory;

    public StreamFile(){
        super();
    }

    public String buildPath(String relativeFilePath) {
        return getRootDirectory() + relativeFilePath;
    }

    public boolean saveFileInDirectory(String path, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        boolean fileCreated;
        boolean directoryCreated = true;

        File file = new File(path);
        if(!file.getParentFile().exists())
            directoryCreated = file.getParentFile().mkdirs();

        if(directoryCreated && !file.exists()) {
            fileCreated = file.createNewFile();

            try(OutputStream outputStream = new FileOutputStream(file)){
                outputStream.write(byteArrayOutputStream.toByteArray());
            }
            LOG.debug("File loaded in directory -> {}", fileCreated);
            return fileCreated;
        }
        return false;
    }

    private String getRootDirectory() {
        if (rootDirectory == null || rootDirectory.equals("temp"))
            rootDirectory = System.getProperty("java.io.tmpdir");

        return Paths.get(rootDirectory).toString();
    }

}
