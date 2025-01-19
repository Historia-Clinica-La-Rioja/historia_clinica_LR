package ar.lamansys.sgh.shared.infrastructure;

import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DigitalSignatureDocumentContentDto {

	private Long id;

	private FilePathBo content;

	private String hash;
}
