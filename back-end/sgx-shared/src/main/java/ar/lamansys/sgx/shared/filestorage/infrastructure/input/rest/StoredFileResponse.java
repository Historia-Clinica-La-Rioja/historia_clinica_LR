package ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest;

import java.net.URLConnection;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;

public class StoredFileResponse {
	public static ResponseEntity<Resource> sendFile(FileContentBo content, String filename, MediaType contentType) {
		return ResponseEntity.ok()
				.header(
						HttpHeaders.CONTENT_DISPOSITION,
						String.format("attachment; filename=\"%s\"", filename)
				)
				.contentType(contentType)
				.contentLength(content.size)
				.body(new InputStreamResource(content.stream));
	}

	public static ResponseEntity<Resource> sendFile(FileContentBo content, String filename, String mediaType) {
		return sendFile(content, filename, MediaType.parseMediaType(mediaType));
	}

	public static String getContentType(String fileName) {
		return URLConnection.guessContentTypeFromName(fileName);
	}

	public static ResponseEntity<Resource> sendFile(StoredFileBo storedFileBo) {
		return sendFile(storedFileBo.resource, storedFileBo.filename, storedFileBo.contentType);
	}
}
