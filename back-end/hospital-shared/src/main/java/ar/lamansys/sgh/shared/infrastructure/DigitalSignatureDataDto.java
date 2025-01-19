package ar.lamansys.sgh.shared.infrastructure;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DigitalSignatureDataDto {

	private String cuil;

	private Integer personId;

	private List<DigitalSignatureDocumentContentDto> documents;

	private Integer institutionId;
}
