package ar.lamansys.sgh.clinichistory.domain.document.digitalsignature;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MultipleDigitalSignatureCallbackBo {

	@JsonProperty("documentos")
	private List<DigitalSignatureCallbackBo> documents;
}
