package net.pladema.federar.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocalIdSearchResponse {
	private String resourceType;
	private String id;
	private String type;
	private Integer total;
	private List<FederarEntryPayload> entry;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public LocalIdSearchResponse(@JsonProperty("resourceType") String resourceType, @JsonProperty("type") String type,
			@JsonProperty("id") String id, @JsonProperty("total") Integer total,
			@JsonProperty("entry") List<FederarEntryPayload> entry) {
		this.resourceType = resourceType;
		this.type = type;
		this.id = id;
		this.total = total;
		this.entry = entry;
	}

	public Optional<Integer> getNationalId() {
		return entry.stream()
				.filter(element -> element.getResource().getResourceType().equals(FederarResourcePayload.PATIENT_TYPE))
				.findFirst().map(element -> Integer.valueOf(element.getResource().getId()));
	}
}
