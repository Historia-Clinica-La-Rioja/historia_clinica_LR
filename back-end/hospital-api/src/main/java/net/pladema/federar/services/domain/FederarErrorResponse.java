package net.pladema.federar.services.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FederarErrorResponse {

	private String resourceType;
	private List<FederarIssuePayload> issue;
	
	
}
