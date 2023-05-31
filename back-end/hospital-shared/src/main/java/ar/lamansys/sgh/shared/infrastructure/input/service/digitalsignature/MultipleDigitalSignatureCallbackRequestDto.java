package ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

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
public class MultipleDigitalSignatureCallbackRequestDto {

	@JsonProperty("documentos")
	private List<DigitalSignatureCallbackRequestDto> documents;
}
