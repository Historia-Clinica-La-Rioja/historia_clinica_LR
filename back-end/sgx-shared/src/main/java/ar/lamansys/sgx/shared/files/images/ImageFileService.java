package ar.lamansys.sgx.shared.files.images;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.files.FileService;

@Component
public class ImageFileService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageFileService.class);

    private static final String OUTPUT = "Output -> {}";

    private static final String FILE_EXTENSION = ".b64image";

    private static final Charset ENCODING = StandardCharsets.UTF_8;

	private final FileService fileService;

    public ImageFileService(FileService fileService){
        this.fileService = fileService;
    }
	public String buildCompletePath(String fileRelativePath){
		LOG.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
		String path = fileService.buildCompletePath(fileRelativePath);
		LOG.debug(OUTPUT, path);
		return path;
	}

	public String createFileName(){
        return fileService.createFileName(FILE_EXTENSION);
    }

    public String readImage(String path) {
        LOG.debug("Input parameter -> path {}", path);
		String result = fileService.readFileAsString(path, ENCODING);
		LOG.debug(OUTPUT, imageDataToString(result));
		return result;
    }

    public boolean saveImage(String relativePath, String fileName, String generatedFrom, String imageData) {
        LOG.debug("Input parameters -> relativePath {}, imageData {}", relativePath, imageDataToString(imageData));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.writeBytes(imageData.getBytes());
		var result = fileService.saveStreamInPath(relativePath, fileName, generatedFrom,false, os);
		LOG.debug(OUTPUT, result);
		return result.getId() != null;
    }

    private String imageDataToString(String imageData){
        return "(" +
                "exists imageData='" + (imageData != null) + '\'' +
                ')';
    }
}
