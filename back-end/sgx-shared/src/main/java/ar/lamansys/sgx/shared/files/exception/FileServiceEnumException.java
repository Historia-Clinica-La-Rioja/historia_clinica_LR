package ar.lamansys.sgx.shared.files.exception;

import lombok.Getter;

@Getter
public enum FileServiceEnumException {
	WRITE_INVALID,
	READ_INVALID,
	NON_EXIST,
	IS_NOT_DIRECTORY,
	CANNOT_CREATE_FOLDER,
	SAVE_IOEXCEPTION,
	INSUFFICIENT_STORAGE,
	;
}
