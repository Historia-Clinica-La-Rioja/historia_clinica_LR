package net.pladema.sgx.pdf;

import net.pladema.internation.repository.masterdata.entity.DocumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
class StreamFile {

    private static final Logger LOG = LoggerFactory.getLogger(StreamFile.class);

    @Value("${internment.document.directory:temp}")
    private String rootDirectory;

    private final Map<Short, String> documentTypes;

    private static final String PDF_EXTENSION = ".pdf";

    private static final String RELATIVE_DIRECTORY = "institution/{institutionId}/internment/{internmentEpisodeId}/{documentType}/";

    public StreamFile(){
        documentTypes = new HashMap<>();
        documentTypes.put(DocumentType.ANAMNESIS, "anamnesis");
        documentTypes.put(DocumentType.EVALUATION_NOTE, "evolutionNote");
        documentTypes.put(DocumentType.EPICRISIS, "epicrisis");
    }

    String buildPath(Integer institutionId, Integer internmentEpisodeId, Short documentType) {
        return getRootDirectory() + RELATIVE_DIRECTORY
                .replace("{institutionId}",institutionId.toString())
                .replace("{internmentEpisodeId}", internmentEpisodeId.toString())
                .replace("{documentType}", documentTypes.get(documentType)) +
                buildUUIDName() + PDF_EXTENSION;
    }

    String buildDownloadName(Long documentId, Short documentType){
        String name = documentTypes.get(documentType) + "_" + documentId;
        return buildDownloadName(name);
    }

    String buildDownloadName(String name){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        name = name + "_" + formattedDateTime + PDF_EXTENSION;
        return name;
    }

    String buildUUIDName(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
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
