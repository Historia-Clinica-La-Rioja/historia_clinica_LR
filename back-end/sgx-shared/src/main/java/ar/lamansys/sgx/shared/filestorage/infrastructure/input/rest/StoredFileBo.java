package ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest;

import org.springframework.http.MediaType;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import lombok.Getter;

public class StoredFileBo {
	@Getter
    public final FileContentBo resource;
	protected final MediaType contentType;
	public final String filename;

	public StoredFileBo(FileContentBo resource, MediaType contentType, String filename) {
		this.resource = resource;
		this.contentType = contentType;
		this.filename = filename;
	}

	public StoredFileBo(FileContentBo resource, String contentType, String filename) {
		this(resource, MediaType.parseMediaType(contentType), filename);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("StoredFileBo{");
		sb.append("contentType='").append(contentType).append('\'');
		sb.append(", filename='").append(filename).append('\'');
		sb.append('}');
		return sb.toString();
	}

	public String getContentType() {
		return contentType.toString();
	}
}
