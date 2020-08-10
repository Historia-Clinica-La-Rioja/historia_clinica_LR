package net.pladema.pdf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
class StreamFile {

    private static final Logger LOG = LoggerFactory.getLogger(StreamFile.class);

    @Value("${internment.document.directory:temp}")
    private String rootDirectory;

    private static final String PDF_EXTENSION = ".pdf";

    public StreamFile(){
        super();
    }

    String buildPath(String relativeDocumentPath) {
        return getRootDirectory() + relativeDocumentPath + PDF_EXTENSION;
    }

    String buildDownloadName(Long documentId, String documentType){
        String name = documentType + "_" + documentId;
        return buildDownloadName(name);
    }

    String buildDownloadName(String name){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        name = name + "_" + formattedDateTime + PDF_EXTENSION;
        return name;
    }

    boolean loadFileInDirectory(String path, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
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
