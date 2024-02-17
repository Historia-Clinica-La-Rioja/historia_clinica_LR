package ar.lamansys.sgx.shared.files.images;

import static ar.lamansys.sgx.shared.filestorage.application.FileContentBo.fromString;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class ImageFileService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageFileService.class);

    private static final String OUTPUT = "Output -> {}";

    private static final Charset ENCODING = StandardCharsets.UTF_8;

	private final FileService fileService;

	public FilePathBo buildCompletePath(String fileRelativePath){
		LOG.debug("Input paramenter -> fileRelativePath {}", fileRelativePath);
		var path = fileService.buildCompletePath(fileRelativePath);
		LOG.debug(OUTPUT, path);
		return path;
	}

	public String createUuid(){
        return fileService.createUuid();
    }

    public String readImage(FilePathBo path) {
        LOG.debug("Input parameter -> path {}", path);
		String result = fileService.readFileAsString(path, ENCODING).orElse(null);
		LOG.debug(OUTPUT, imageDataToString(result));
		return result;
    }

    public boolean saveImage(FilePathBo path, String fileName, String generatedFrom, String imageData) {
        LOG.debug("Input parameters -> relativePath {}, imageData {}", path.relativePath, imageDataToString(imageData));

		var result = fileService.saveStreamInPath(path, fileName, generatedFrom,false, fromString(imageData));
		LOG.debug(OUTPUT, result);
		return result.getId() != null;
    }

    private String imageDataToString(String imageData){
        return "(" +
                "exists imageData='" + (imageData != null) + '\'' +
                ')';
    }
}
