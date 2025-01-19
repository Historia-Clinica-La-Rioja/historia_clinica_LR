package ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DigitalSignatureStatusDto {

	@NotNull
	@JsonProperty("success")
	private Boolean success;

	@JsonProperty("msg")
	private String msg;
}
