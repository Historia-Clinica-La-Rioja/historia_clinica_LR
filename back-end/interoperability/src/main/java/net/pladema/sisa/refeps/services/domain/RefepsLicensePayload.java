package net.pladema.sisa.refeps.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefepsLicensePayload {

	private String licenseNumber;

	private String status;

	private String state;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public RefepsLicensePayload(@JsonProperty("matricula") String licenseNumber, @JsonProperty("estado") String status,  @JsonProperty("jurisdiccion") String state) {
		this.licenseNumber = licenseNumber;
		this.status = status;
		this.state = state;
	}
}
