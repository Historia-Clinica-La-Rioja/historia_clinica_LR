package ar.lamansys.sgx.shared.filestorage.application;

public class BucketObjectException extends RuntimeException {
	public final String errorCode;
	public final FilePathBo path;

	public BucketObjectException(String errorCode, FilePathBo path) {
		this.errorCode = errorCode;
		this.path = path;
	}
}
