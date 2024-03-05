package ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest;

import java.util.function.Supplier;

import org.springframework.http.MediaType;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;

public class BlobLazyFileBo {
	protected final Supplier<FileContentBo> resourceLoader;
	protected final MediaType contentType;
	protected final String filename;

	public BlobLazyFileBo(Supplier<FileContentBo> resourceLoader, MediaType contentType, String filename) {
		this.resourceLoader = resourceLoader;
		this.contentType = contentType;
		this.filename = filename;
	}

}
