package ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;

public class StoredFileResponse {
	private StoredFileResponse() {

	}
	public static ResponseEntity<Resource> sendFile(FileContentBo content, String filename, MediaType contentType) {
		return ResponseEntity.ok()
				.header(
						HttpHeaders.CONTENT_DISPOSITION,
						contextDispositionValue(filename)
				)
				.contentType(contentType)
				.contentLength(content.size)
				.body(new InputStreamResource(content.stream));
	}

	public static ResponseEntity<Resource> sendFile(FileContentBo content, String filename, String mediaType) {
		return sendFile(content, filename, MediaType.parseMediaType(mediaType));
	}

	public static ResponseEntity<Resource> sendFile(StoredFileBo blob) {
		return sendFile(
				blob.resource,
				blob.filename,
				blob.contentType
		);
	}
	public static ResponseEntity<Resource> sendStoredBlob(BlobLazyFileBo blob) {
		return sendFile(
				blob.resourceLoader.get(),
				blob.filename,
				blob.contentType
		);
	}

	public static ResponseEntity<Resource> sendGeneratedBlob(GeneratedBlobBo blob) {
		return sendFile(
				blob.resource,
				blob.filename,
				blob.contentType
		);
	}

	private static String contextDispositionValue(String filename) {
		return ContentDisposition.builder("attachment")
				.filename(filename, StandardCharsets.UTF_8) // Establecer el nombre de archivo codificado en UTF-8
				.build()
				.toString();

	}
}
