package ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository;

import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BucketObjectInfo {
	public final FilePathBo path;
	public final long size;
	public final String checksum;


}
