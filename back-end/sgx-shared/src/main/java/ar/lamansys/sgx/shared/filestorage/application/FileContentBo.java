package ar.lamansys.sgx.shared.filestorage.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.Getter;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

public class FileContentBo {
	public final long size;
	@Getter
	public final InputStream stream;

	private FileContentBo(long size, InputStream stream) {
		this.size = size;
		this.stream = stream;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BucketObject{");
		sb.append("size=").append(size);
		sb.append(", stream=").append(stream!=null);
		sb.append('}');
		return sb.toString();
	}

	public static FileContentBo fromString(String textData) {
		byte[] imageContent = textData.getBytes();
		return new FileContentBo(
				imageContent.length,
				new ByteArrayInputStream(imageContent)
		);
	}

	public static FileContentBo fromResource(Resource resource) throws IOException {
		return new FileContentBo(
				resource.contentLength(),
				resource.getInputStream()
		);
	}

	public static FileContentBo fromBytes(byte[] data) throws IOException {
		return fromResource(
				new ByteArrayResource(data)
		);
	}

}
