package ar.lamansys.sgx.shared.filestorage.application;

public class BucketObjectAccessException extends BucketObjectException {
	public BucketObjectAccessException(FilePathBo path) {
		super("SAVE_IOEXCEPTION", path);
	}
}
