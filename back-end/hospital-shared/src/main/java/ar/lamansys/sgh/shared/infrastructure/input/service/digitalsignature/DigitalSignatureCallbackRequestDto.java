package ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DigitalSignatureCallbackRequestDto {

	@JsonProperty("status")
	private DigitalSignatureStatusDto status;

	@JsonProperty("metadata")
	private Map<String, String> metadata;

	@JsonProperty("hash")
	private String hash;

	@JsonProperty("documento")
	private String signatureHash;
	
	public Long getDocumentId(){
		return Long.parseLong(metadata.get("documentId"));
	}

	public Integer getPersonId(){
		return Integer.parseInt(metadata.get("personId"));
	}

}
