package net.pladema.sisa.refeps.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefepsLicenseSearchResponse {

	private String resultMessage;
	private List<RefepsLicensePayload> response;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public RefepsLicenseSearchResponse(@JsonProperty("resultado") String resultMessage,
									   @JsonProperty("matriculas") List<RefepsLicensePayload> response) {
		this.response = response;
		this.resultMessage = resultMessage;
	}

}
