package ar.lamansys.sgx.shared.files.exception;

import lombok.Getter;

@Getter
public class FileServiceException extends RuntimeException {

	private FileServiceEnumException code;

	public FileServiceException(FileServiceEnumException code, String errorMessage){
		super(errorMessage);
		this.code = code;
	}

	public String getCodeInfo() {
		return code.name();
	}
}
