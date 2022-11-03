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
	public String buildPath(String fileRelativePath){
		LOG.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
		String path = fileService.buildRelativePath(fileRelativePath);
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

    public boolean saveImage(String path, String imageData) {
        LOG.debug("Input parameters -> path {}, imageData {}", path, imageDataToString(imageData));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.writeBytes(imageData.getBytes());
		boolean result = fileService.saveStreamInPath(path, false, os);
		LOG.debug(OUTPUT, result);
		return result;
    }

    private String imageDataToString(String imageData){
        return "(" +
                "exists imageData='" + (imageData != null) + '\'' +
                ')';
    }
}
