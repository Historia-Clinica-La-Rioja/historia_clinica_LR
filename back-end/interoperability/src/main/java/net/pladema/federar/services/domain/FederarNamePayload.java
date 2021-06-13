package net.pladema.federar.services.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FederarNamePayload {
	private String use;
	private String text;
	private String family;

	@JsonProperty("_family")
	private FederarFamilyPayload familyExtension;
	
	private List<String> given;
	
}
