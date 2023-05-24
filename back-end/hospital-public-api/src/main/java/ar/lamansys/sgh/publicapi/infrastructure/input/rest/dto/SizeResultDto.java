package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class SizeResultDto implements Serializable {
	private Integer idMove;
	private Integer size;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public SizeResultDto(@JsonProperty("idMove") Integer idMove,
						 @JsonProperty("size") Integer size
	) {
		this.idMove = idMove;
		this.size = size;

	}
}
