package net.pladema.federar.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocalIdSearchResponse {
	private String resourceType;
	private String id;
	private String type;
	private Integer total;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public LocalIdSearchResponse(@JsonProperty("resourceType") String resourceType, @JsonProperty("type") String type,
			@JsonProperty("id") String id, @JsonProperty("total") Integer total) {
		this.resourceType = resourceType;
		this.type = type;
		this.id = id;
		this.total = total;

	}

}
