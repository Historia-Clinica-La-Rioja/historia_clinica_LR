package net.pladema.digitalsignature.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class FirmadorDocumentRequestBody {

	private String documento;

	private Object metadata;

}
