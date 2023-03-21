package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EPacServerProtocol {

	SFTP((short) 1, "SFTP"),
	DICOM_WEB((short) 2, "DICOM WEB"),
	;

	private final Short id;
	private final String description;
}
