package net.pladema.federar.services.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public class FederarNamePayload {

	@EqualsAndHashCode.Include
	private String use;
	@EqualsAndHashCode.Include
	private String text;
	@EqualsAndHashCode.Include
	private String family;

	@JsonProperty("_family")
	@EqualsAndHashCode.Include
	private FederarFamilyPayload familyExtension;

	@EqualsAndHashCode.Include
	private List<String> given;
	
}
