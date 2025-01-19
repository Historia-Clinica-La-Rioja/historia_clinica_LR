package ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest;

import org.springframework.http.MediaType;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GeneratedBlobBo {
	protected final FileContentBo resource;
	protected final MediaType contentType;
	protected final String filename;
}
