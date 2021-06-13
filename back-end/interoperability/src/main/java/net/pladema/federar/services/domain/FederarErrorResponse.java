package net.pladema.federar.services.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FederarErrorResponse {

	private String resourceType;
	private List<FederarIssuePayload> issue;
	
	public String getDiagnostics(){
		return getIssue().stream().map(FederarIssuePayload::getDiagnostics)
				.filter(Objects::nonNull)
				.collect( Collectors.joining( " | " ) );
	}
}
