package ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StoredFileBo {
    public final Resource resource;;
	public final String contentType;
    public final String filename;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("StoredFileBo{");
		sb.append("contentType='").append(contentType).append('\'');
		sb.append(", filename='").append(filename).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
