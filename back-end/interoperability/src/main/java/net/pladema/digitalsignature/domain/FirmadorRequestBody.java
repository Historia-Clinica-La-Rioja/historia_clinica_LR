package net.pladema.digitalsignature.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class FirmadorRequestBody {

	private String cuil;

	private String type;

	private String urlRedirect;

	private List<FirmadorDocumentRequestBody> documentos;
}
