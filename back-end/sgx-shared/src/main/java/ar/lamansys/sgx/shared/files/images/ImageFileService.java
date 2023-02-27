package ar.lamansys.sgx.shared.files.images;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.files.FileService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ImageFileService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageFileService.class);

    private static final String OUTPUT = "Output -> {}";

    private static final Charset ENCODING = StandardCharsets.UTF_8;

	private final FileService fileService;

	public String buildCompletePath(String fileRelativePath){
		LOG.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
		String path = fileService.buildCompletePath(fileRelativePath);
		LOG.debug(OUTPUT, path);
		return path;
	}

	public String createUuid(){
        return fileService.createUuid();
    }

    public String readImage(String path) {
        LOG.debug("Input parameter -> path {}", path);
		String result = fileService.readFileAsString(path, ENCODING);
		LOG.debug(OUTPUT, imageDataToString(result));
		return result;
    }

    public boolean saveImage(String relativePath, String uuid, String generatedFrom, String imageData) {
        LOG.debug("Input parameters -> relativePath {}, imageData {}", relativePath, imageDataToString(imageData));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.writeBytes(imageData.getBytes());
		var result = fileService.saveStreamInPath(relativePath, uuid, generatedFrom,false, os);
		LOG.debug(OUTPUT, result);
		return result.getId() != null;
    }

    private String imageDataToString(String imageData){
        return "(" +
                "exists imageData='" + (imageData != null) + '\'' +
                ')';
    }
}
