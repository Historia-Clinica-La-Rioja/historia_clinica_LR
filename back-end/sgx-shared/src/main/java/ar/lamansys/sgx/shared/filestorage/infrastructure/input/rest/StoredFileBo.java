package ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StoredFileBo {
	@Getter
    public final FileContentBo resource;
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
