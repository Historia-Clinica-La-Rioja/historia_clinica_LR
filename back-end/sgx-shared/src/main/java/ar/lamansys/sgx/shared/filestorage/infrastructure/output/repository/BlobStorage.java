package ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;

public interface BlobStorage {

	FilePathBo buildPath(String relativeFilePath);

	boolean existFile(FilePathBo path);

	BucketObjectInfo put(FilePathBo path, FileContentBo object) throws IOException;

	FileContentBo get(FilePathBo path) throws IOException;

	BucketObjectInfo getInfo(FilePathBo path) throws IOException;

	void delete(FilePathBo path);

	boolean existBucket();

	default BucketObjectInfo put(FilePathBo path, FileContentBo object, boolean override) throws IOException {
		if (override || !existFile(path)) {
			return put(path, object);
		}
		return getInfo(path);
	}

	default String readFileAsString(FilePathBo path, Charset charset) throws IOException{
		ByteArrayInputStream is = new ByteArrayInputStream(get(path).stream.readAllBytes());
		int numberOfBytes = is.available();
		byte[] bytes = new byte[numberOfBytes];
		is.read(bytes, 0, numberOfBytes);
		return new String(bytes, charset);
	}

	Map<String, Object> status();
}
