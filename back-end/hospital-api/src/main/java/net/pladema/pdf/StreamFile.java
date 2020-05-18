package net.pladema.pdf;

import lombok.NoArgsConstructor;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@NoArgsConstructor
class StreamFile {

    private static final Logger LOG = LoggerFactory.getLogger(StreamFile.class);

    @Value("${internment.document.directory:temp}")
    private String rootDirectory;

    private final String PDF_EXTENSION = ".pdf";

    private final Map<Short, String> documentTypes = new HashMap<Short, String>(){{
        put(DocumentType.ANAMNESIS, "anamnesis");
        put(DocumentType.EVALUATION_NOTE, "evolutionNote");
        put(DocumentType.EPICRISIS, "epicrisis");
    }};

    private static final String relativeDirectory = "institution/{institutionId}/internment/{internmentEpisodeId}/{documentType}/";

    String buildPath(Integer institutionId, Integer internmentEpisodeId, Short documentType) {
        return getRootDirectory() + relativeDirectory
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
        boolean fileCreated = false;
        boolean directoryCreated = true;
        try {
            File file = new File(path);
            if(!file.getParentFile().exists())
                directoryCreated = file.getParentFile().mkdirs();

            if(directoryCreated && !file.exists()) {
                fileCreated = file.createNewFile();
                OutputStream outputStream = new FileOutputStream(file);
                outputStream.write(byteArrayOutputStream.toByteArray());
                outputStream.close();
            }
            LOG.debug("File loaded in directory -> {}", fileCreated);
            return fileCreated;
        }
        catch(IOException ex){
            throw new IOException(ex);
        }
    }

    private String getRootDirectory() {
        if (rootDirectory != null && rootDirectory.equals("temp")) {
            return System.getProperty("java.io.tmpdir");
        }
        return rootDirectory;
    }

}
