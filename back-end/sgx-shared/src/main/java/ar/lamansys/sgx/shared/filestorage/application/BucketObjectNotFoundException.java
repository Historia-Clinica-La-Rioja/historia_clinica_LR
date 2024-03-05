package ar.lamansys.sgx.shared.filestorage.application;

public class BucketObjectNotFoundException extends BucketObjectException {
	public BucketObjectNotFoundException(FilePathBo path) {
		super("NON_EXIST", path);
	}
}
